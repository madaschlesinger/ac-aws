resource "aws_ecs_cluster" "jpmc-ecs-cluster" {
  name = "jpmc-ecs-cluster"
}

resource "aws_autoscaling_group" "terraform-ecs-cluster" {
    availability_zones = ["eu-west-1a"]
    name = "jpmc-ecs-cluster"
    min_size = 2
    max_size = 2
    desired_capacity = 2
    health_check_type = "EC2"
    launch_configuration = "${aws_launch_configuration.ecs.name}"
    vpc_zone_identifier = ["${aws_subnet.slaves.id}"]
    tag {
          key = "Name"
          value = "Terraform ECS Cluster"
          propagate_at_launch = true
        }
}

resource "aws_iam_instance_profile" "ecs" {
    name = "ecs_profile_terraform"
    roles = ["weave-ecs-role"]
    path = "/"
}

resource "aws_launch_configuration" "ecs" {
    image_id = "ami-a77b0ac7"
    instance_type = "${var.instance_type}"
    security_groups = ["${aws_security_group.slaves.id}"]
    iam_instance_profile = "${aws_iam_instance_profile.ecs.name}"
    key_name = "${var.aws_key_name}"
    associate_public_ip_address = true
    user_data = "#!/bin/bash\necho ECS_CLUSTER='${aws_ecs_cluster.jpmc-ecs-cluster.name}' > /etc/ecs/ecs.config"

}

resource "aws_ecs_service" "jenkins" {
  name = "jenkins"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.jenkins.arn}"
  desired_count = 1

}

resource "aws_ecs_task_definition" "jenkins" {
  family = "jenkins"
  container_definitions = "${file("tasks/jenkins.json")}"

  volume {
    name = "jenkins-home"
    host_path = "/ecs/jenkins-home"
  }
}

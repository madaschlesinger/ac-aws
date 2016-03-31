resource "aws_ecs_cluster" "jpmc-ecs-cluster" {
  name = "jpmc-ecs-cluster"
}

resource "aws_autoscaling_group" "terraform-ecs-cluster" {
    availability_zones = ["eu-west-1a"]
    name = "jpmc-ecs-cluster"
    min_size = 3
    max_size = 4
    desired_capacity = 3
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
    name = "ecs_launch_profile"
    roles = ["${aws_iam_role.terraform_ecs_instance.id}"]
    path = "/"
}

resource "aws_launch_configuration" "ecs" {
    image_id = "ami-77ab1504"
    instance_type = "${var.instance_type}"
    security_groups = ["${aws_security_group.slaves.id}"]
    iam_instance_profile = "${aws_iam_instance_profile.ecs.name}"
    key_name = "${var.aws_key_name}"
    associate_public_ip_address = false
    user_data = "#!/bin/bash\necho ECS_CLUSTER='${aws_ecs_cluster.jpmc-ecs-cluster.name}' > /etc/ecs/ecs.config"

}

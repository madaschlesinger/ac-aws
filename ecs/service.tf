resource "aws_ecs_service" "jenkins" {
  name = "jenkins"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.jenkins.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.jenkins.id}"
    container_name = "jenkins"
    container_port = 8080
  }

}

resource "aws_ecs_task_definition" "jenkins" {
  family = "jenkins"
  container_definitions = "${file("tasks/jenkins.json")}"

  volume {
    name = "jenkins-home"
    host_path = "/ecs/jenkins-home"
  }
}


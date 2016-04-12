resource "aws_ecs_service" "services-zeromq-server" {
  name = "services-zeromq-server"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.services-zeromq-server.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.java-zeromq-server.id}"
    container_name = "services-zeromq-server"
    container_port = 8084
  }

}

resource "aws_ecs_task_definition" "services-zeromq-server" {
  family = "services-zeromq-server"
  container_definitions = "${file("${path.module}/tasks/services-zeromq-server.json")}"

}


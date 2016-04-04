resource "aws_ecs_service" "services-sqs-server" {
  name = "services-sqs-server"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.services-sqs-server.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.java-sqs-server.id}"
    container_name = "services-sqs-server"
    container_port = 8080
  }

}

resource "aws_ecs_task_definition" "services-sqs-server" {
  family = "services-sqs-server"
  container_definitions = "${file("tasks/services-sqs-server.json")}"

}


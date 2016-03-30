resource "aws_ecs_service" "services-indexed-rest" {
  name = "services-indexed-rest"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.services-indexed-rest.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.java-service-indexed.id}"
    container_name = "services-indexed-rest"
    container_port = 8080
  }

}

resource "aws_ecs_task_definition" "services-indexed-rest" {
  family = "services-indexed-rest"
  container_definitions = "${file("tasks/services-indexed-rest.json")}"

}


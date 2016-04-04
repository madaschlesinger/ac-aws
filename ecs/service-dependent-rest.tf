resource "aws_ecs_service" "services-dependent-rest" {
  name = "services-dependent-rest"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.services-dependent-rest.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.java-zeromq-client.id}"
    container_name = "services-zeromq-client"
    container_port = 8080
  }

}

resource "aws_ecs_task_definition" "services-dependent-rest" {
  family = "services-zeromq-client"
  container_definitions = "${file("tasks/services-dependent-rest.json")}"

}


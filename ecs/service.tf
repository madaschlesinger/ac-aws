resource "aws_ecs_service" "nginx" {
  name = "nginx"
  cluster = "${aws_ecs_cluster.jpmc-ecs-cluster.id}"
  task_definition = "${aws_ecs_task_definition.nginx.arn}"
  desired_count = 1
  iam_role = "${aws_iam_role.terraform_ecs_instance.arn}"

  load_balancer {
    elb_name = "${aws_elb.nginx.id}"
    container_name = "nginx"
    container_port = 80
  }

}

resource "aws_ecs_task_definition" "nginx" {
  family = "nginx"
  container_definitions = "${file("tasks/nginx.json")}"

}


resource "aws_iam_role" "terraform_ecs_instance" {
    name = "terraform_ecs_instance"
    assume_role_policy = "${file("policies/ecs-role.json")}"
}

resource "aws_iam_role_policy" "terraform_ecs_instance_role_policy" {
  name     = "ecs_instance_role_policy"
  policy   = "${file("policies/AmazonEC2ContainerServiceRole.json")}"
  role     = "${aws_iam_role.terraform_ecs_instance.id}"
}

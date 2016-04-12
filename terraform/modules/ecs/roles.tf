resource "aws_iam_role" "terraform_ecs_instance" {
    name = "terraform_ecs_instance"
    assume_role_policy = "${file("${path.module}/policies/ecs-role.json")}"
}

resource "aws_iam_role_policy" "terraform_ecs_instance_role_policy" {
  name     = "ecs_instance_role_policy"
  policy   = "${file("${path.module}/policies/AmazonEC2ContainerServiceRole.json")}"
  role     = "${aws_iam_role.terraform_ecs_instance.id}"
}

resource "aws_iam_policy" "sqs-policy" {
    name = "sqs-policy"
    description = "SQS policy to avoid storing credentials to access that"
    policy =   "${file("${path.module}/policies/SQSFullAccessRole.json")}" 
}

resource "aws_iam_policy_attachment" "sqs-policy-attachment" {
    name = "sqs-policy-attachment"
    roles = ["${aws_iam_role.terraform_ecs_instance.name}"]
    policy_arn = "${aws_iam_policy.sqs-policy.arn}"
}

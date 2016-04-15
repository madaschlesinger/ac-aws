resource "aws_iam_role" "terraform_mesos_instance" {
    name = "terraform_mesos_instance"
    assume_role_policy = "${file("${path.module}/policies/mesos-role.json")}"
}


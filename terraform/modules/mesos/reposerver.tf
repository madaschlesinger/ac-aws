resource "aws_instance" "repository" {
    ami = "ami-8b8c57f8"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.shared-services.id}"]
    subnet_id = "${aws_subnet.shared-services.id}"
    associate_public_ip_address = "false"
    tags {
        Name = "YUM-Repo"
        Group = "${var.tag_value}"
    }
}

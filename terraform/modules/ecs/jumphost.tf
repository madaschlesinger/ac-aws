resource "aws_instance" "jumphost" {
    ami = "ami-8b8c57f8"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.jumphosts.id}"]
    subnet_id = "${aws_subnet.jumphosts.id}"
    associate_public_ip_address = true
    tags {
        Name = "JumpHost-ECS"
        Group = "${var.tag_value}"
    }
}

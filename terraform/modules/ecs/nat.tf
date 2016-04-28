resource "aws_instance" "nat" {
    ami = "ami-14913f63"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.NAT-Gateway.id}"]
    subnet_id = "${aws_subnet.natgateways.id}"
    associate_public_ip_address = "true"
    source_dest_check = "false"
    tags {
        Name = "NAT Gateway"
        Group = "${var.tag_value}"
    }
}

output "nat-ecs" {
    value = "${aws_instance.nat.id}"
}

resource "aws_instance" "nat" {
    ami = "ami-14913f63"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.NAT-Gateway.id}"]
    subnet_id = "${aws_subnet.natgateways.id}"
    associate_public_ip_address = "true"
    source_dest_check = "false"
    tags {
        Name = "NAT Gateway for Mesos"
        Group = "${var.tag_value}"
    }
}

resource "aws_route_table_association" "nat-gw-routing" {
    subnet_id = "${aws_subnet.natgateways.id}"
    route_table_id = "${aws_route_table.r.id}"
}

output "nat-mesos" {
    value = "${aws_instance.nat.id}"
}

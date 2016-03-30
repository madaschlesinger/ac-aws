# Need to create an Internal Network Interface and register a record into Route53

resource "aws_instance" "zookeeper" {
    ami = "ami-8b8c57f8"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.shared-services.id}"]
    subnet_id = "${aws_subnet.shared-services.id}"
    associate_public_ip_address = true
    tags {
        Name = "Zookeeper"
    }
}

resource "aws_route53_record" "zookeeper-dev" {
   zone_id = "${aws_route53_zone.dev.zone_id}"
   name = "zookeeper"
   type = "A"
   ttl = "300"
   records = ["${aws_instance.zookeeper.private_ip}"]
}

resource "aws_route_table_association" "zookeeper-routing" {
    subnet_id = "${aws_subnet.shared-services.id}"
    route_table_id = "${aws_route_table.r.id}"
}

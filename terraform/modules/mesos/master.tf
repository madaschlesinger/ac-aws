resource "aws_instance" "master" {
    ami = "${var.master_ami}"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.master.id}"]
    subnet_id = "${aws_subnet.master.id}"
    associate_public_ip_address = "true"
    user_data = "ZOOKEEPER=${aws_instance.zookeeper.private_ip}"
    source_dest_check = "false"
    tags {
        Name = "Mesos Master"
    }
}

resource "aws_route_table_association" "master-routing" {
    subnet_id = "${aws_subnet.master.id}"
    route_table_id = "${aws_route_table.r.id}"
}

output "mesos-console" {
    value = "http://${aws_instance.master.public_dns}:5050"
}



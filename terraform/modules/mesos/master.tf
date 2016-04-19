## First create a stable point to call master

resource "aws_network_interface" "mesos-master" {
    subnet_id = "${aws_subnet.master.id}"
    private_ips = ["10.1.7.50"]
    security_groups = ["${aws_security_group.master.id}"]
    source_dest_check = "false"
    attachment {
        instance = "${aws_instance.master.id}"
        device_index = 1
    }
}

resource "aws_instance" "master" {
    ami = "ami-d7d35fa4"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.master.id}"]
    subnet_id = "${aws_subnet.master.id}"
    associate_public_ip_address = "true"
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



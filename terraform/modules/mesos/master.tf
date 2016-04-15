resource "aws_instance" "master" {
    ami = "ami-d4fc71a7"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.master.id}"]
    subnet_id = "${aws_subnet.master.id}"
    associate_public_ip_address = "true"
    source_dest_check = "false"
    user_data = "#!/bin/bash\nexport EXTERNAL_IP='${public_ip}' && /tmp/user_data.sh"
    tags {
        Name = "Mesos Master"
    }
}

resource "aws_route_table_association" "master-routing" {
    subnet_id = "${aws_subnet.master.id}"
    route_table_id = "${aws_route_table.r.id}"
}



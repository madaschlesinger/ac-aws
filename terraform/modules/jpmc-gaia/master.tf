resource "aws_instance" "master" {
    ami = "${var.master_ami}"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${var.security_group}"]
    subnet_id = "${var.aws_subnet_master}"
    associate_public_ip_address = "false"
    user_data = "ZOOKEEPER=${aws_instance.zookeeper.private_ip}\nYUM_REPO_IP=${aws_instance.yum.private_ip}"
    source_dest_check = "false"
    tags {
        Name = "Mesos Master"
        Group = "${var.tag_value}"
    }
}

output "mesos-console" {
    value = "http://${aws_instance.master.private_ip}:5050"
}



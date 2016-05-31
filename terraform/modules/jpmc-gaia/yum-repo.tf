resource "aws_instance" "yum" {
    ami = "${var.yum-repo_ami}"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["${var.security_group}"]
    private_ip = "${var.yum-repo_private_ip}"
    user_data  = "#!/bin/bash\nmount /dev/xvde /rpms"
    subnet_id = "${var.aws_subnet_shared-services}"
    tags {
        Name = "YUM-Repo"
        Group = "${var.tag_value}"
    }
}

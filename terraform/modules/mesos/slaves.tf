resource "aws_autoscaling_group" "terraform-mesos-cluster" {
    availability_zones = ["eu-west-1a"]
    name = "jpmc-mesos-cluster"
    min_size = 1
    max_size = 3
    desired_capacity = 1
    health_check_type = "EC2"
    launch_configuration = "${aws_launch_configuration.mesos-slaves.name}"
    vpc_zone_identifier = ["${aws_subnet.mesos_slaves.id}"]
    tag {
          key = "Name"
          value = "Mesos Slave Cluster"
          propagate_at_launch = true
        }
}

resource "aws_iam_instance_profile" "mesos" {
    name = "mesos_launch_profile"
    roles = ["${aws_iam_role.terraform_mesos_instance.id}"]
    path = "/"
}

resource "aws_launch_configuration" "mesos-slaves" {
    image_id = "ami-1c0b876f"
    instance_type = "${var.instance_type}"
    security_groups = ["${aws_security_group.slaves.id}"]
    iam_instance_profile = "${aws_iam_instance_profile.mesos.name}"
    key_name = "${var.aws_key_name}"
    user_data = "MASTER=${aws_elb.master-elb.dns_name}"
    associate_public_ip_address = false

}

## Create now an autoscaling group of public accessible slaves

resource "aws_autoscaling_group" "terraform-public-mesos-cluster" {
    availability_zones = ["eu-west-1a"]
    name = "jpmc-public-mesos-cluster"
    min_size = 1
    max_size = 3
    desired_capacity = 1
    health_check_type = "EC2"
    launch_configuration = "${aws_launch_configuration.public-mesos-slaves.name}"
    vpc_zone_identifier = ["${aws_subnet.mesos_slaves_public.id}"]
    tag {
          key = "Name"
          value = "Mesos Public Slave Cluster"
          propagate_at_launch = true
        }
}

resource "aws_iam_instance_profile" "mesos-public" {
    name = "mesos_launch_profile_public"
    roles = ["${aws_iam_role.terraform_mesos_instance.id}"]
    path = "/"
}

resource "aws_launch_configuration" "public-mesos-slaves" {
    image_id = "ami-1c0b876f"
    instance_type = "${var.instance_type}"
    security_groups = ["${aws_security_group.slaves_public.id}"]
    iam_instance_profile = "${aws_iam_instance_profile.mesos-public.name}"
    key_name = "${var.aws_key_name}"
    user_data = "MASTER=${aws_elb.master-elb.dns_name}"
    associate_public_ip_address = true

}

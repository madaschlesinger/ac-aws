resource "aws_autoscaling_group" "terraform-mesos-cluster" {
    availability_zones = ["${var.AZ}"]
    name = "jpmc-mesos-cluster"
    min_size = 1
    max_size = 3
    desired_capacity = 1
    health_check_type = "EC2"
    launch_configuration = "${aws_launch_configuration.mesos-slaves.name}"
    vpc_zone_identifier = ["${var.aws_subnet_mesos_slaves}"]
    tag {
          key = "Name"
          value = "Mesos Slave Cluster"
          propagate_at_launch = true
        }
}


resource "aws_launch_configuration" "mesos-slaves" {
    image_id = "${var.slaves_ami}"
    instance_type = "${var.instance_type}"
    security_groups = ["${var.security_group}"]
    iam_instance_profile = "JPMC_AppServices_S3_Bucket_Access"
    key_name = "${var.aws_key_name}"
    user_data = "ZOOKEEPER=${aws_instance.zookeeper.private_ip}\nMASTER=${aws_instance.master.private_ip}\nYUM_REPO_IP=${aws_instance.yum.private_ip}"
    associate_public_ip_address = false

}

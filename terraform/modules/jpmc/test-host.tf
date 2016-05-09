resource "aws_instance" "test_Adaptive_host_private" {
    ami = "ami-a83208c2"
    instance_type = "t2.micro"
    key_name = "${var.aws_key_name}"
    security_groups = ["sg-751a530d"]
    subnet_id = "subnet-907637ad"
    associate_public_ip_address = false
    tags {
        Name = "AdaptiveTest"
        Group = "${var.tag_value}"
    }
}

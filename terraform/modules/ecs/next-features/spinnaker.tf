# Test Deployment for Netflix CI Spinnaker product

resource "aws_instance" "spinnaker" {
    ami = "ami-44c94837"
    instance_type = "m4.xlarge"
    key_name = "${var.aws_key_name}"
    security_groups = ["${aws_security_group.ELBS.id}"]
    subnet_id = "${aws_subnet.ELBS.id}"
    associate_public_ip_address = "true"
    tags {
        Name = "Spinnaker"
    }
}

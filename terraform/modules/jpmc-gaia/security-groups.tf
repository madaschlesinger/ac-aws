resource "aws_security_group" "slaves" {
  name = "Mesos Slaves SG - Terraform"
  description = "Allow access only from internal "
  vpc_id = "${var.aws_vpc_mesos}"

  ingress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["${split(",", var.sec_groups_mesos)}"]
  }

}


resource "aws_security_group" "master" {
  name = "Mesos Masters SG - Terraform"
  description = "Allow access only from internal "
  vpc_id = "${var.aws_vpc_mesos}"

  ingress {
     from_port = 22
     to_port = 22
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
     from_port = 80
     to_port = 80
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {
     from_port = 443 
     to_port = 443 
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 5000
     to_port = 5000
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 5050 
     to_port = 5050
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 8080
     to_port = 8080
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 2181
     to_port = 2181
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 5051
     to_port = 5051
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 8500
     to_port = 8500
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 6784
     to_port = 6784
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 6783
     to_port = 6783
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 8300
     to_port = 8300
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]

  }
  ingress {

     from_port = 8301
     to_port = 8301
     protocol = "tcp"
     cidr_blocks = ["0.0.0.0/0"]
  }

}


resource "aws_security_group" "shared-services" {
  name = "Shared Services"
  description = "Allow access only on services ports"
  vpc_id = "${var.aws_vpc_mesos}"

  ingress {
      from_port = 22
      to_port = 22
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
      from_port = 2181
      to_port = 2181
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }


}

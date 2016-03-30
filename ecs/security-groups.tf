resource "aws_security_group" "jumphosts" {
  name = "Jump Hosts SG - ECS Terraform"
  description = "Allow only SSH access on the Jump Host"
  vpc_id = "${aws_vpc.terraform_vpc.id}"

  ingress {
      from_port = 22
      to_port = 22
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
      from_port = 1
      to_port = 65535 
      protocol = "tcp"
      cidr_blocks = ["10.0.1.0/24","10.0.2.0/24","10.0.3.0/24","10.0.4.0/24"]
  }
}

resource "aws_security_group" "webservers" {
  name = "Web Servers SG - ECS Terraform"
  description = "Allow access only from internal "
  vpc_id = "${aws_vpc.terraform_vpc.id}"

  ingress {
      from_port = 80
      to_port = 80
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["10.0.1.0/24"]
  }
}

resource "aws_security_group" "slaves" {
  name = "ECS Instances SG - ECS Terraform"
  description = "Allow access only from internal "
  vpc_id = "${aws_vpc.terraform_vpc.id}"

  ingress {
      from_port = 0
      to_port = 0 
      protocol = "-1"
      cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["0.0.0.0/0"]
  }
}


resource "aws_security_group" "ELBS" {
  name = "Public facing ELBs- ECS Terraform"
  description = "Allow access only on standard ports"
  vpc_id = "${aws_vpc.terraform_vpc.id}"

  ingress {
      from_port = 80
      to_port = 80
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["0.0.0.0/0"]
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["10.0.1.0/24"]
  }
}

resource "aws_security_group" "shared-services" {
  name = "Shared Services"
  description = "Allow access only on services ports"
  vpc_id = "${aws_vpc.terraform_vpc.id}"

  ingress {
      from_port = 22
      to_port = 22
      protocol = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["0.0.0.0/0"]
      from_port = 0
      to_port = 0
      protocol = "-1"
      cidr_blocks = ["10.0.1.0/24"]
  }
}


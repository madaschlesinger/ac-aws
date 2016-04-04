[
  {
    "name": "services-sqs-client",
    "image": "johnisadaptive/services-sqs-client",
    "cpu": 400,
    "memory": 400,
    "essential": true,
    "portMappings": [
      {
        "containerPort": 8080,
        "hostPort": 8087
      }
    ],
    "environment": [
        {
          "name": "QUEUE_NAME",
          "value": "terraform-queue"
        },
        {
          "name": "AWS_ACCESS_KEY_ID",
          "value": "AKIAJPVTE7OSSEBAY4WA"
        },
        {
          "name": "AWS_SECRET_ACCESS_KEY",
          "value": "mbIwRxhOwTh8yKvDKC8svt/XRHdGivRDVD+opEpp"
        }
      ]

  }
]

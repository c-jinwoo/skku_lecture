FROM ubuntu:latest

RUN apt-get update
RUN apt-get install -y git

RUN apt-get update && apt-get install -y \
    python3 \
    python3-pip \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /root
RUN mkdir A B C files
RUN git clone https://github.com/c-jinwoo/assignment_week8.git
RUN touch files/a.txt
RUN touch files/b.txt
RUN touch files/c.txt

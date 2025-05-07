FROM php:8.4.5-fpm-alpine3.21
COPY --from=ghcr.io/roadrunner-server/roadrunner:2023.1.1 /usr/bin/rr /usr/bin/rr

RUN apk update && apk add \
    bash \
    git \
    nano \
    htop \
    fish \
    libpq-dev \
    tzdata \
    postgresql-client \
    zip \
    wget make autoconf g++

ENV TZ=Europe/Minsk
ADD https://github.com/mlocati/docker-php-extension-installer/releases/latest/download/install-php-extensions /usr/local/bin/

RUN chmod +x /usr/local/bin/install-php-extensions && \
    install-php-extensions pdo_pgsql intl amqp sockets bcmath pcntl

RUN install-php-extensions @composer

RUN pecl install pcov
RUN pecl install mongodb
RUN pecl install redis

RUN apk update && \
    apk add freetype-dev \
    libjpeg-turbo-dev \
    curl-dev \
    libpng-dev && \
    docker-php-ext-configure gd --with-freetype=/usr/include/ --with-jpeg=/usr/include/ && \
    docker-php-ext-install gd curl opcache && \
    docker-php-ext-enable pcov mongodb redis

USER root

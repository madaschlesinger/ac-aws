FROM nginx

EXPOSE 80 443

CMD hostname -i > /usr/share/nginx/html/index.html && nginx -g 'daemon off;'

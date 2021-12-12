docker run -d \
      -p 6831:6831/udp \
      -p 6832:6832/udp \
      -p 16686:16686 \
      -p 14250:14250 \
      --name=jaeger-all-in-one \
      jaegertracing/all-in-one:1.29 \
      --log-level=debug




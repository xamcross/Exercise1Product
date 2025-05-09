FROM ghcr.io/graalvm/graalvm-ce:ol7-java17-22.3.3 AS build
WORKDIR /app
COPY . .
RUN gu install native-image \
 && ./gradlew nativeCompile

FROM debian:bullseye-slim
COPY --from=build /app/build/native/nativeCompile/product-native /myapp
ENTRYPOINT ["/myapp"]

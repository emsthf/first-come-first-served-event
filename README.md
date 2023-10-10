# 선착순 쿠폰 시스템

## 요구사항
선착순 100명에게 할인쿠폰을 제공 

- 선착순 100명에게만 제공
- 101개 이상 지급되면 안된다
- 순간적으로 몰리는 트래픽을 버틸 수 있어야 함

### DB의 쿠폰 발행 개수 count 쿼리로 쿠폰 발행 개수를 조절하는 경우
```java
public void apply(Long userId) {
    long count = couponRepository.count();

    // 발급 가능 개수 초과시 발급X
    if (count > 100) {
        return;
    }

    couponRepository.save(new Coupon(userId));
}
```
두 개 이상의 스레드가 공유 데이터에 접근해서 동시에 작업하려해 레이스 컨디션이 발생한다.

<br>

레이스 컨디션을 해결하는 방법은 여러가지가 있다.

JAVA에서 지원하는 Synchronized를 사용할 수도 있겠지만 Synchronized는 서버가 여러 대가 된다면 레이스 컨디션이 다시 발생하므로 적절하지 않다.

MySQL, Redis를 활용한 락을 구현해서 해결할 수도 있지만 쿠폰 개수에 대한 정합성만 원하는 것인데 쿠폰 사용에서부터 발급까지 락이 걸리는 구간이 길어져 성능이 떨어질 수 있다.

여기선 Redis를 사용하는 방법으로 해결한다.
레이스 컨디션은 

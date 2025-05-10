```mermaid
sequenceDiagram
    participant 사용자
    participant BE as Product API

    사용자 ->> BE: 상품 ID로 상세 조회 요청
    activate BE
    
    break 상품이 존재하지 않으면
        BE --x 사용자: 상품 없음 예외
    end
    
    BE -->> 사용자: 상품 상세 응답
    deactivate BE
```

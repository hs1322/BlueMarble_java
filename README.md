# 미니프로젝트 (2025.02.13~2025.2.17) 1차원배열 또는 2차원배열을 활용한 게임 제작 - 브루마블(blue marble)

# 개요
본 프로젝트는 Java를 활용한 텍스트 기반 보드게임으로, 플레이어들이 주사위를 굴려 보드를 이동하며, 부동산을 구매하고 거래하는 방식으로 진행됩니다. 특정 칸에서는 특수한 이벤트가 발생하며, 최종적으로 파산하지 않고 가장 많은 자산을 보유한 플레이어가 승리합니다.

# 시스템 구성

1. 게임 보드 (맵 시스템)

  - 13x13 크기의 보드 (실제 이동 가능한 경로는 외곽 40칸)

  - 특수 칸: 출발점, 무인도, 우주여행, 기부, 기부복권

  - 일반 칸: 구매 가능한 부동산 (도시, 국가 등)

2. 플레이어 시스템

  - 최대 4명 참여 가능
  
  - 각 플레이어는 초기 자본(1,200,000원) 보유
  
  - 각 플레이어는 특정 위치에서 시작하며, 주사위를 굴려 이동
  
  - 땅을 구매하고, 다른 플레이어가 해당 칸을 지나가면 통행료를 지불받음
  
  - 자금이 부족할 경우 부동산을 매각 가능
  
  - 자금이 마이너스가 될 경우 파산 처리 

# 주요 기능

1. 게임 진행

 - 플레이어는 차례대로 주사위를 굴려 이동
  
 - 동일한 숫자의 주사위(더블)가 나오면 한 번 더 이동 가능
  
 - 이동 후 해당 칸에서 발생하는 이벤트 처리

2. 부동산 시스템

 - 플레이어는 도착한 칸이 빈 땅일 경우 구매 가능
  
 - 소유자가 있는 경우 통행료 지불 필요
  
 - 자금 부족 시 땅을 매각하여 자금 확보 가능

3. 특수 칸 이벤트

 - 출발점: 도착 시 300,000원 지급
  
 - 무인도: 도착 시 3턴 동안 휴식 (주사위를 굴려 탈출 가능)
  
 - 우주여행: 원하는 거리만큼 이동 가능
  
 - 기부: 100,000원 자동 기부
  
 - 기부복권: 현재 기부된 금액을 당첨자에게 지급

4) 파산 및 게임 종료

 - 플레이어가 파산하면 게임에서 제외
  
 - 남은 플레이어가 1명일 경우 게임 종료
  
 - 최종적으로 가장 많은 자산을 보유한 플레이어가 승리

4. 예외 처리 및 추가 기능

 - 잘못된 입력 처리: 플레이어의 입력이 유효하지 않은 경우 재입력 요청

 - 자동 매각 기능: 플레이어가 자금 부족 시 보유 부동산을 자동으로 매각하여 통행료나 기부금을 납부할 수 있도록 처리

 - 게임 상태 출력: 현재 보드 상태 및 플레이어 자산을 실시간으로 출력

# 플로우차트(flow chart)


# 주요기능







# 개선사항
1. 게임 진행 관련 개선
 - 턴 진행 방식 개선
    - 현재는 단순히 p++로 플레이어를 교체하는 방식이므로, 게임 흐름을 관리하는 별도 턴 시스템 구성

2. 부동산 시스템 개선
 - 부동산 업그레이드 기능 추가
    - 현재는 단순한 부동산 구매 및 통행료 개념만 존재. 건물 업그레이드(예: 집 → 호텔) 기능을 추가.

- 통행료 인상 기능 추가
    - 같은 플레이어가 같은 땅을 여러 번 방문하면 통행료가 오르는 기능을 추가.
    - ex) 1회 방문 시 기본 통행료, 2회 방문 시 1.5배, 3회 방문 시 2배 등으로 조정.

- 부동산 매각 방식 개선
    - 현재는 단순히 판매만 가능하지만, 플레이어 거래하는 기능(경매 시스템 등)을 추가.

3. 특수 칸 이벤트 개선

- 황금열쇠 추가
    - 기부칸 일부를 황금열쇠로 변경하여 도착시 여러가지 이벤트를 제공

- 우주여행 칸 개선
    - 현재는 원하는 거리만큼 이동할 수 있지만, 목적지를 선택하는 방식으로 변경.
    - ex) "목적지를 선택하시겠습니까?" → 출발점, 무인도, 랜덤 장소 중 선택 가능하도록 변경.

4. 게임 밸런스 조정

 - 게임 종료 조건 추가
     - 마지막 남은 플레이어가 승리하는 것이 아니라, 일정 턴 수 이후 총 자산 평가를 통해 승자를 결정하는 방식도 추가. 너무 긴 게임 진행을 방지.
  

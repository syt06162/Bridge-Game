# Bridge-Game
Bridge Game with Java GUI (CAU 2022-1)  
시연 영상, 브릿지 게임 규칙, 구현 사항 요약, 사용 기술 및 패턴에 대해 설명.  
게임 규칙은 2022-1 중앙대학교 소프트웨어공학 이찬근 교수님의 과제 설명서를 참고하였음.   

## 시연 영상
[![Video Label](http://img.youtube.com/vi/LiVTxAA5ijE/0.jpg)](https://youtu.be/LiVTxAA5ijE?t=0s)

## 브릿지 게임 규칙
- 시작 지점: START, 도착 지점: END 로 표시
- 게임 시작 시 START 에 모든 플레이어의 말이 위치
- 각 플레이어는 자신의 턴에 주사위를 굴려 몇 칸 이동할지 결정 (roll)
- 플레이어는 주사위의 값에서 bridge 카드 개수를 뺀 만큼 이동
- 플레이어는 주사위를 한 번 굴려서 나온 값 만큼 앞 혹은 뒤로 이동하도록 선택   
 이렇게 하여 진행하는 칸을 희생해서 도구 카드를 집을 가능성을 높일 수 있음
- 어떤 플레이어가 최초로 END 를 넘으면 남아 있는 나머지 플레이어들은 더 이상 뒤로는 이동할
수 없음
- 가장 처음 END 를 넘은 플레이어는 7 점, 두 번째 플레이어는 3 점, 세 번째 플레이어는 1 점

- 플레이어는 각 도구 카드에 대해 다음과 같은 점수를 얻는다.
  Philips Driver (P) : 1점
  Hammer (H) : 2점
  Saw (S): 3점

- 한명의 플레이어만 보드에 남으면 게임 종료
- 총점 = 도구 카드 점수 + 순위 점수, 총점이 가장 높은 플레이어가 승리

- 플레이어는 이동 시 다리를 건너거나 혹은 건너지 않고 갈 것인지 결정
- 다리를 건넌 플레이어는 다리 카드를 한 장 얻음
- 플레이어는 자신의 턴일때 주사위를 굴리지 않고 쉬어서, 다리 카드 1개를 제거 가능 (rest)
- 이 게임에서 다리는 항상 왼쪽 셀과 오른쪽 셀을 연결한다고 가정

## 구현 사항 요약
- Java GUI로 플레이 가능, CLI도 맵 로드까지 지원
- 커스터마이징한 맵을 자유롭게 로드 할 수 있음. 단, map 파일의 형식 및 규칙이 알맞아야함.
- 게임 규칙에 알맞게, 구현 요구사항 모두 구현
- 추가적으로 '공지'(notice) 탭을 구성하여, 플레이를 더 편리하게 함

## 사용 기술
- Java GUI (swing)

## 적용한 패턴
- OOP
- MVC 패턴 (Java의 Observable, Observer 이용)

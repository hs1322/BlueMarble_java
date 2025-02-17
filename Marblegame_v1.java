package marbleGame;

import java.util.Random;
import java.util.Scanner;

public class Marblegame_v1 {
	static String map[][][] = new String[13][13][4]; // 맵 정보 저장
	static int user[][] = new int[4][4]; // 최대 4인용, 4인의 {가진 돈, 유저 index, 유저가 위치한 x좌표, 유저가 위치한 y좌표}
	static String userLand[][] = new String[user.length][30]; // 유저가 소유한 맵 이름 [유저 index][유저가 소유한 땅 이름]
	static String userLand1[][][] = new String[user.length][30][2];// 유저가 소유한 땅 좌표[유저 index][유저 땅이름좌표][x,y좌표]
	static Scanner in = new Scanner(System.in);
	static Random r = new Random();
	static int doubleCnt = 0, userNum  = 0, donation = 0, distance = 0;
	static int[] rest= {0,0,0,0};
	public static void main(String[] args) {
		System.out.println("''∧  ∧");
		System.out.println(" (-з-)");
		System.out.println("┏━〇〇━━━━━━━━┓");
		System.out.println("┃   부루마불     ┃");
		System.out.println("┃    조각성     ┃");
		System.out.println("┗┳┳━━━━━━━┳┳┛");
		System.out.println(" ┗┛       ┗┛");
		int p = 1;
		int pcnt=0;
		mapSeting();
		userNum = userSeting();
		mapPrint();
		while (true) { // 게임시작
			if(rest[p-1] != 0&&map[user[p-1][3]][user[p-1][2]][0].equals("감옥")) { // 감옥칸 시작
				System.out.println("-------- 땅 정보------------");
				System.out.println("감옥");
				System.out.print(p + "번 플레이어 차례\n주사위 굴리기 enter입력");
				in.nextLine();
				dice();
				rest[p-1]--;
				if(rest[p-1]!=0) {
					System.out.println(rest[p-1]+"번 남았습니다.");
				}else {
					System.out.println("감옥 탈출");
				}
			}else if(map[user[p-1][3]][user[p-1][2]][0].equals("우주여행")){ // 우주여행칸 시작 
				doubleCnt = 0;
				System.out.println("---------------------------------------------------------------------------------------");
				System.out.print(p + "번 플레이어 차례\n 우주여행\n");
				moving(p,distance);
				mapPrint();
				if(map[user[p-1][3]][user[p-1][2]][2].equals(" ")) {
					landInfo(user[p-1][2],user[p-1][3]);
					for(;;) {
						System.out.println("땅을 구매 하시겠습니까? yes or no");
						String a = in.nextLine();
						if(a.equals("yes")) {
							landBuy(p,user[p-1][2],user[p-1][3]);
							break;
						}else if(a.equals("no")) {
							break;
						}else {
							System.out.println("잘못 입력하셨습니다. 다시입력해주세요");
						}
					}	
				}else{
					if(map[user[p-1][3]][user[p-1][2]][2].equals(Integer.toString(p))) {
						landInfo(user[p-1][2],user[p-1][3]);
						System.out.println(map[user[p-1][3]][user[p-1][2]][2] + "님이 보유중입니다.");
					}else {
						landInfo(user[p-1][2],user[p-1][3]);
						System.out.println(map[user[p-1][3]][user[p-1][2]][2] + "님이 보유중입니다." + "통행료" + map[user[p-1][3]][user[p-1][2]][1] + "를 지불하세요");
						tollpay(p,Integer.parseInt(map[user[p-1][3]][user[p-1][2]][2]),Integer.parseInt(map[user[p-1][3]][user[p-1][2]][1]));
					}
				}
			}
			else if(rest[p-1] != -1){ // 일반 칸 
				System.out.println("---------------------------------------------------------------------------------------");
				System.out.print(p + "번 플레이어 차례\n주사위 굴리기 enter입력");
				in.nextLine();
				int num = dice(); // 주사위 굴리기
				moving(p,num); // 말 이동
				try {
				    Thread.sleep(1000); // 1초 동안 지연
				} catch (InterruptedException e) {
				    e.printStackTrace();
				}
				mapPrint(); // 실시간 맵 출력
				if(map[user[p-1][3]][user[p-1][2]][0].equals("기부")) { // 기부칸 도착 이벤트 처리
					System.out.println("-------- 땅 정보------------");
					System.out.println("기부\n플레이어" +p+ "님이 100000원을 기부했습니다.");
					if(user[p-1][0] <100000 && userLand[p-1][0]==null){
						donation += 100000;
						user[p-1][0] -= 100000;
					}else if(user[p-1][0]<100000) {
						while(user[p-1][0]<100000) {
							System.out.println("금액이 부족합니다.");
							landSell(p);
						}
						donation += 100000;
						user[p-1][0] -= 100000;
					}else {
						donation += 100000;
						user[p-1][0] -= 100000;
					}
				}else if(map[user[p-1][3]][user[p-1][2]][0].equals("기부복권")) { // 기부 복권칸 도착 이벤트 처리
					System.out.println("-------- 땅 정보------------");
					System.out.println("기부복권 모인금액: "+ donation + "\n 복권당첨! 모인금액을 드립니다!");
					user[p-1][0] += donation;
					donation = 0;
				}else if(map[user[p-1][3]][user[p-1][2]][0].equals("감옥")) { // 감옥칸 도착 이벤트 처리
					if(rest[p-1]==0) {
						rest[p-1]=3;
					}
					System.out.println("-------- 땅 정보------------");
					System.out.println("감옥");
					System.out.println("감옥 도착 3턴간 휴식 ㅠㅠ");
				}else if(map[user[p-1][3]][user[p-1][2]][0].equals("우주여행")) { // 우주여행칸 도착 이벤트 처리
					System.out.println("-------- 땅 정보------------");
					System.out.println("우주여행\n 우주 여행을 하시겠습니까? yes or no");
					for(;;) {
						String a = in.nextLine();
						if(a.equals("yes")) {
							System.out.println("거리를 얼만큼 이동하시겠습니까? 최대거리 39 ");
							distance = 0;
							while(distance==0) {
								distance=in.nextInt();
								in.nextLine();
								if(1>distance || distance>39) {
									distance = 0;
									System.out.println("잘못 입력하셨습니다.\n다시 입력하세요.");
								}
							}
							break;
						}else if(a.equals("no")) {
							break;
						}else {
							System.out.println("잘못 입력하셨습니다. 다시입력해주세요");
						}
					}
					
				}else if(map[user[p-1][3]][user[p-1][2]][0].equals("출발")) { // 출발지 도착 이벤트 처리
					System.out.println("-------- 땅 정보------------");
					System.out.println("출발");
					System.out.println("출발지 도착 월급 20만원");
				}else { // 일반칸 도착 이벤트 처리
					if(map[user[p-1][3]][user[p-1][2]][2] != null && map[user[p-1][3]][user[p-1][2]][2].equals(" ")) { // 땅 소유주가 없을경우
						landInfo(user[p-1][2],user[p-1][3]);
						for(;;) {
							System.out.println("땅을 구매 하시겠습니까? yes or no");
							String a = in.nextLine();
							if(a.equals("yes")) {
								landBuy(p,user[p-1][2],user[p-1][3]);
								break;
							}else if(a.equals("no")) {
								break;
							}else {
								System.out.println("잘못 입력하셨습니다. 다시입력해주세요");
							}
						}
					}
					else { // 땅 소유주가 있을경우
						if(map[user[p-1][3]][user[p-1][2]][2].equals(Integer.toString(p))) {
							landInfo(user[p-1][2],user[p-1][3]);
							System.out.println(map[user[p-1][3]][user[p-1][2]][2] + "님이 보유중입니다.");
						}else {
							landInfo(user[p-1][2],user[p-1][3]);
							System.out.println(map[user[p-1][3]][user[p-1][2]][2] + "님이 보유중입니다." + "통행료" + map[user[p-1][3]][user[p-1][2]][1] + "를 지불하세요");
							tollpay(p,Integer.parseInt(map[user[p-1][3]][user[p-1][2]][2]),Integer.parseInt(map[user[p-1][3]][user[p-1][2]][1]));
						}
					}
				}
				userInfo();
				System.out.println("현재모인 기부금액: " + donation);
			}
			if(user[p-1][0]<0) { // 파산처리
				System.out.println(p + "번플레이어 파산ㅜㅜ");
				user[p-1][0]=9999999;
				pcnt++;
				rest[p-1]=-1;
				doubleCnt=0;
			}
			if(pcnt==userNum-1) { // 게임종료 조건
				System.out.println("게임 종료");
				for(int i=0;i<4;i++ ) {
					if(user[i][0]!=9999999) {
						System.out.println((i+1)+"번님 우승");
					}
				}
				break;
			}
			if(doubleCnt == 0) {
				p++;
				if(p > userNum)
					p = 1;
			}
		}
	
	}
	
	public static void landInfo(int x, int y) { // 맵정보 출력
		System.out.println("-------- 땅 정보------------");
		System.out.println(map[y][x][0]);
		System.out.println("토지 가격: " + map[y][x][1]);
		System.out.println("토지 소유자: " + map[y][x][2]);
	}
	
	public static void landBuy(int pNum, int x, int y) { // 땅 구매
		if(user[pNum-1][0] >= Integer.parseInt(map[y][x][1])) {
			user[pNum-1][0] -= Integer.parseInt(map[y][x][1]);
			for(int i = 0;; i++) {
				if(userLand[pNum-1][i] == null) {
					userLand[pNum-1][i] = map[y][x][0];
					break;
				}
			}
			map[y][x][2] = Integer.toString(pNum);
		}else {
			System.out.println("금액이 부족합니다.");
		}
	}
	
	public static void tollpay(int p1, int p2, int money) { // 통행료 지불
		while(true) {
			if(user[p1-1][0] >= money) {
				user[p1-1][0] -= money;
				user[p2-1][0] += money;
				return;
			}else if(user[p1-1][0] <money && userLand[p1-1][0]==null) {
				user[p1-1][0] -= money;
				user[p2-1][0] += money;
				return;
			}else {
				System.out.println("금액이 " + (money - user[p1-1][0]) + "만큼 부족합니다.");
				landSell(p1);
			}
		}
	}
	
	
	public static void moving(int pNum, int dice) { // 말 이동
		if(user[pNum-1][3] == 1) {
			map[user[pNum-1][3]-1][user[pNum-1][2]][pNum-1] = "0";
		}else if(user[pNum-1][3] == 11) {
			map[user[pNum-1][3]+1][user[pNum-1][2]][pNum-1] = "0";
		}else if(user[pNum-1][2] == 1) {
			map[user[pNum-1][3]][user[pNum-1][2]-1][pNum-1] = "0";
		}else if(user[pNum-1][2] == 11) {
			map[user[pNum-1][3]][user[pNum-1][2]+1][pNum-1] = "0";
		}
		while(dice>0) {
			if((user[pNum-1][2]+1) < map[0].length-1 && user[pNum-1][3] == 1 ) {
				user[pNum-1][2]++;			
			}
			else if((user[pNum-1][2]) == map[0].length-2 && user[pNum-1][3]+1 < map.length-1) {
				user[pNum-1][3]++;				
			}
			else if(user[pNum-1][3] == map.length-2 && user[pNum-1][2]-1 > 0) {
				user[pNum-1][2]--;
			}
			else if(user[pNum-1][2] == 1 && user[pNum-1][3]-1 > 0) {
				user[pNum-1][3]--;		
			}
			if(user[pNum-1][2] == 11 && user[pNum-1][3] == 11) {
				System.out.println("출발지 도착! money + 200000");
				user[pNum-1][0] += 200000;
			}
			dice--;
		}
		if(user[pNum-1][3] == 1) {
			map[user[pNum-1][3]-1][user[pNum-1][2]][pNum-1] = Integer.toString(pNum);
		}else if(user[pNum-1][3] == 11) {
			map[user[pNum-1][3]+1][user[pNum-1][2]][pNum-1] = Integer.toString(pNum);
		}else if(user[pNum-1][2] == 1) {
			map[user[pNum-1][3]][user[pNum-1][2]-1][pNum-1] = Integer.toString(pNum);
		}else if(user[pNum-1][2] == 11) {
			map[user[pNum-1][3]][user[pNum-1][2]+1][pNum-1] = Integer.toString(pNum);
		}
	}
	
	public static int dice() { // 주사위 굴리기
		int dice1 = r.nextInt(6)+1;
		int dice2 = r.nextInt(6)+1;
		System.out.println("┏━━━━━━━━━━━━━━━┓");
		System.out.println("┃   ["+dice1+"]+["+dice2+"]="+(dice1+dice2)+"   ┃");
		System.out.println("┗━━━━━━━┳┳━━━━━━┛");
		System.out.println(" (\\__/) ┃┃");
		System.out.println(" (•ㅅ•)핫┗┛");
		System.out.println(" /......づ");
		if(dice1 == dice2) {
			System.out.println("┌─o─┐");
			System.out.println("│ 더 │");
			System.out.println("│ 블 │");
			System.out.println("│   │");
			System.out.println("│ 한 │ハハ");
			System.out.println("│ 번 │゜ω゜ )");
			System.out.println("│ 더 │／/");
			System.out.println("└─o─┘ O");
			doubleCnt++;
		}
		else 
			doubleCnt = 0;
		return dice1+dice2;
	}
	
	public static void mapSeting() { // 맵 초기 세팅
		int x=0, y=0;
		while(true) {
			for(int i = 0; i < map[0][0].length; i++) {
				map[x][y][i] = "0";
			}
			if(x < map[0].length-1 && y==0) {
				x++;
			}
			else if(x == map[0].length-1 && y < map.length-1) {
				y++;
			}
			else if(y == map.length-1 && x > 0) {
				x--;
			}
			else if(x == 0 && y > 0) {
				y--;
			}
	
			if(x == 0 && y == 0) {
				break;
			}
		}
		for(int i = 2; i < map.length-2; i++) {
			for(int j = 2; j < map[0].length-2; j++) {
				map[i][j][0] = "";
			}
		}
		map[1][1][0]="기부복권";
		map[11][11][0]="출발";
		map[11][1][0]="감옥";
		map[1][11][0]="우주여행";
		String[] mapl1= {"대만","기부","중국","가나","경주","싱가포르","기부","쿠바","터키"};
		String[] mapl2= {"칠레","기부","챠드","페루","인도","도미니카","기부","독일","몽골"};
		String[] mapl3= {"콜롬비아","기부","오만","기부","부산","뉴질랜드","포르투갈","에버랜드","노르웨이"};
		String[] mapl4= {"일본","호주","보스니아","이탈리아","기부","영국","미국","기부","대한민국"};
		//라인1
		for(int i=0; i<4;i++) {
			for(int j=0; j<9; j++) {
				if(i==0) {
				  map[11][10-j][i]=mapl1[j];
				}else if(map[11][10-j][0].equals("기부")) {
				  map[11][10-j][1]="100000";
				}else if(i==1) {
				  map[11][10-j][i]="200000";
				}else if(i==2) {
				  map[11][10-j][i]=" ";
				}else if(i==3) {
				  map[11][10-j][i]=" ";
				}
			}
		}
		//라인2
		for(int i=0; i<4;i++) {
			for(int j=0; j<9; j++) {
				if(i==0) {
				  map[10-j][1][i]=mapl2[j];
				}else if(map[10-j][1][0].equals("기부")) {
				  map[10-j][1][i]="100000";
				}else if(i==1) {
				  map[10-j][1][i]="300000";
				}else if(i==2) {
				  map[10-j][1][i]=" ";
				}else if(i==3) {
				  map[10-j][1][i]=" ";
				}
			}
		}
		//라인3
		for(int i=0; i<4;i++) {
			for(int j=0; j<9; j++) {
				if(i==0) {
				  map[1][j+2][i]=mapl3[j];
				}else if(map[1][j+2][0].equals("기부")) {
				  map[1][j+2][1]="100000";
				}else if(i==1) {
				  map[1][j+2][i]="400000";
				}else if(i==2) {
				  map[1][j+2][i]=" ";
				}else if(i==3) {
				  map[1][j+2][i]=" ";
				}
			}
		}
		//라인4
		for(int i=0; i<4;i++) {
			for(int j=0; j<9; j++) {
				if(i==0) {
				  map[j+2][11][i]=mapl4[j];
				}else if(map[j+2][11][0].equals("기부")) {
				  map[j+2][11][1]="100000";
				}else if(i==1) {
				  map[j+2][11][i]="500000";
				}else if(i==2) {
				  map[j+2][11][i]=" ";
				}else if(i==3) {
				  map[j+2][11][i]=" ";
				}
			}
		}
	}
	
	public static void mapPrint() { // 현재 맵 정보 출력
		String[][] c=new String[11][11];
		int a=0;
		int x=0;
		int y=0;
		for(int i=0; i<1;) {
			if((y+1==user[3][2]&&user[3][3]==x+1)) {
				c[x][y]="4";	
			}else {
				c[x][y]="";
			}
			if((y+1==user[2][2]&&user[2][3]==x+1)) {
				c[x][y]+="3";	
			}else {
				c[x][y]+="";
			}
			if(y+1==user[1][2]&&user[1][3]==x+1) {
				c[x][y]+="2";	
			}else {
				c[x][y]+="";
			}
			if(y+1==user[0][2]&&user[0][3]==x+1) {
				c[x][y]+="1";	
			}else {
				c[x][y]+=" ";
			}
			if(x==0&&y==0){
				a++;
			}
			if(x==10&&y==0){
				a=2;
			}
			if(x==10&&y==10){
				a=3;
			}
			if(x==0&&y==10){
				a=4;
			}
			if(a==1) {
				x++;
			}else if(a==2) {
				y++;
			}else if(a==3) {
				x--;
			}else if(a==4) {
				y--;
			}else {
				i++;
			}
		}
		System.out.println(c[0][0]+"		 "+c[0][1]+"      "+c[0][2]+"      "+c[0][3]+"      "+c[0][4]+"      "+c[0][5]+"      "+c[0][6]+"      "+c[0][7]+"      "+c[0][8]+"      "+c[0][9]+"		"+c[0][10]);
		System.out.println("	┏━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┓");
		System.out.println("	┃  기부  ┃  콜롬  ┃      ┃      ┃      ┃      ┃  뉴질  ┃  포르  ┃  에버  ┃  노르  ┃  우주  ┃");
		System.out.println("	┃      ┃      ┃  기부  ┃  오만  ┃  기부  ┃  부산  ┃      ┃      ┃      ┃      ┃      ┃");
		System.out.println("	┃  복권  ┃  비아  ┃      ┃      ┃      ┃      ┃  랜드  ┃  투갈  ┃  랜드  ┃  웨이  ┃  여행  ┃");
		System.out.println("	┣━━━━━━╋━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━╋━━━━━━┫");
		System.out.println("	┃      ┃██████████████████████████████████████████████████████████████┃      ┃");
		System.out.println(c[1][0]+"	┃  몽골  ┃██████████████████████████──░░░░░──███████████████████████████┃  일본  ┃	"+c[1][10]);
		System.out.println("	┃      ┃███████████████████████░░░░░░░░░░░░░░░████████████████████████┃      ┃");
		System.out.println("	┣━━━━━━┫████████████████████░░░░░░░░░░░░░░░░░░░░░▒████████████████████┣━━━━━━┫");
		System.out.println("	┃      ┃█████████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░██████████████████┃      ┃");
		System.out.println(c[2][0]+"	┃  독일  ┃████████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█████████████████┃  호주  ┃	"+c[2][10]);
		System.out.println("	┃      ┃██████████████░░░░░░░░░░░░░░░░░█░░░░░░░░░░░░░░░███████████████┃      ┃");
		System.out.println("	┣━━━━━━┫██████████████░░░░░░███▒░░░██░░█▓▒░░░░░█▒░░░░░░▒██████████████┣━━━━━━┫");
		System.out.println("	┃      ┃█████████████░░░░░░░░▒███░▒██▒░██▒░▓█░░█▒░░░░░░░██████████████┃  보스  ┃");
		System.out.println(c[3][0]+"	┃  기부  ┃█████████████░░░░░░░▒█▓▒▓░░░█▒░█░░░█░███▒░░░░░░░██████████████┃      ┃	"+c[3][10]);
		System.out.println("	┃      ┃████████████░░░░░░░░▒▒░█░░░▓█░░█░░▓░█░░▓▒░░░░░░░░█████████████┃  니아  ┃");
		System.out.println("	┣━━━━━━┫████████████░░░░░░░░░░▓█▒░░▓░▒█▓█░█░▒▒░█▒░░░░░░░░█████████████┣━━━━━━┫");
		System.out.println("	┃  도미  ┃████████████░░░░░░░░░███▒▒░░░░░░█░░░░░█▒█░░░░░░░░█████████████┃  이탈  ┃");
		System.out.println(c[4][0]+"	┃      ┃████████████░░░░░░░░░░░░░░░░░░░░█░░░░░██▓░░░░░░░░█████████████┃      ┃	"+c[4][10]);
		System.out.println("	┃  니카  ┃████████████░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░█████████████┃  리아  ┃");
		System.out.println("	┣━━━━━━┫████████████░░░░░░░░░░░░░░░░░▒▒▒███████▒▒▒░░░░░░░█████████████┣━━━━━━┫");
		System.out.println("	┃      ┃████████████░░░░░░░░░░░▒▒▓█▓▓░───────────▒▓▓▓▒░░░█████████████┃      ┃");
		System.out.println(c[5][0]+"	┃  인도  ┃████████████░░░░▒█▓▒░──────────────────────────░▓█████████████┃  기부  ┃	"+c[5][10]);
		System.out.println("	┃      ┃██████████▓─────────────────────────────────────────░█████████┃      ┃");
		System.out.println("	┣━━━━━━┫████████▓░───▒▒────────░▒▒░────────────▒░─────▒░──▒█──████████┣━━━━━━┫");
		System.out.println("	┃      ┃███████▒─█▓──██─────░██████────▒▓████──█▒─────█▓▓▓██──░███████┃      ┃");
		System.out.println(c[6][0]+"	┃  페루  ┃██████▒──██████──────░░░░██────░█▓░▒█─░█▒─────███▒▓█───░██████┃  영국  ┃	"+c[6][10]);
		System.out.println("	┃      ┃█████▓───███▒██──────██████─────█▓─▒█─▒███▒───█░░███────▓█████┃      ┃");
		System.out.println("	┣━━━━━━┫█████────█▓░░██──────█▓░────────█▓─▒█─▒█░─────█████░░░───█████┣━━━━━━┫");
		System.out.println("	┃      ┃████▓────█████▓──────██████─────█▓▒██─▒█░───▒▒▒███████───▓████┃      ┃");
		System.out.println(c[7][0]+"	┃  차드  ┃████▓──────────░░────▒▒▒▒▒░░░───████▓─▒█░───███▓▓█▒▒░────▓████┃  미국  ┃	"+c[7][10]);
		System.out.println("	┃      ┃████▓──░▓▓▓██████───▓▓▓▓█████▒────────░█░─────░▓▓█▓▓─────▒████┃      ┃");
		System.out.println("	┣━━━━━━┫█████──██████░░░░───█████░░░░──────────▒░─────████▓█─────▓████┣━━━━━━┫");
		System.out.println("	┃      ┃█████──────▓█──────────░█─────────────────────░█████─────█████┃      ┃");
		System.out.println(c[8][0]+"	┃  기부  ┃█████▒─────▓█──────────░█─────────────────────▓█░░░▒░───▒█████┃  기부  ┃	"+c[8][10]);
		System.out.println("	┃      ┃██████░─────░───────────▓─────────────────────▒███▓▓░──░██████┃      ┃");
		System.out.println("	┣━━━━━━┫██████▓───────────────────────────────────────────────░███████┣━━━━━━┫");
		System.out.println("	┃      ┃██████████──────────────────────────────────────────▒█████████┃  대한  ┃");
		System.out.println(c[9][0]+"	┃  칠레  ┃████████████▒░░░─────░░░░▒█████████▒░░░░░──────░░░██▓█████████┃      ┃	"+c[9][10]);
		System.out.println("	┃      ┃██████████████████████████████████████████████████████████████┃  민국  ┃");
		System.out.println("	┣━━━━━━╋━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━┳━━━━━━╋━━━━━━┫");
		System.out.println("	┃      ┃      ┃      ┃      ┃  싱가  ┃      ┃      ┃      ┃      ┃      ┃      ┃");
		System.out.println("	┃  감옥  ┃  터키  ┃  쿠바  ┃  기부  ┃      ┃  경주  ┃  가나  ┃  중국  ┃  기부  ┃  대만  ┃  출발  ┃");
		System.out.println("	┃      ┃      ┃      ┃      ┃  포르  ┃      ┃      ┃      ┃      ┃      ┃      ┃");
		System.out.println("	┗━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┻━━━━━━┛");
		System.out.println(c[10][0]+"		 "+c[10][1]+"      "+c[10][2]+"      "+c[10][3]+"      "+c[10][4]+"      "+c[10][5]+"      "+c[10][6]+"      "+c[10][7]+"      "+c[10][8]+"      "+c[10][9]+"		"+c[10][10]);
	}
	
	public static int userSeting() { // 유저 초기값 세팅
		int userNum;
		while(true) {
			System.out.println("인원을 입력해주세요(최대 4인): ");
			userNum = in.nextInt();
			in.nextLine();
			if( 0 < userNum && userNum < 5) {
				System.out.println("인원수: " + userNum + "명");
				break;
			}
			else {
				System.out.println("인원이 맞지 않습니다. 다시 입력해 주세요!");
			}
		}
		for(int i = 0; i < userNum; i++) {
			user[i][0] = 1200000;
			user[i][1] = i;
			user[i][2] = 11;
			user[i][3] = 11;
			map[12][11][i] = Integer.toString(i+1);
		}
		
		return userNum;
		
	}
	
	public static void userInfo() { // 유저정보 출력
		System.out.println("------- 플레이어 정보---------");
		for(int i = 0; i < userNum; i++) {
			if(user[i][0] >= 0) {
				if(user[i][0]==9999999) {
					System.out.print("플레이어" + (i+1) + " 파산 ");
				}else {
					System.out.print("플레이어" + (i+1) + " 보유금액: " + user[i][0] + "원 \t 보유 땅: ");
				}
				for(int j = 0; userLand[i][j] != null; j++) {
					System.out.print(userLand[i][j] + " ");
					if(j==10){
						break;
					}
				}
			}
			System.out.println();
		}
	}
	
	public static void subLand(int userNum, String landName) { // 땅 판매 후 유저의 땅정보 제거
		for(int i = 0;  i < userLand[userNum-1].length; i++ ) {
			if(userLand[userNum-1][i].equals(landName)) {
				for(int j = i+1; j < userLand[userNum-1].length; j++) {
					userLand[userNum-1][j-1] = userLand[userNum-1][j];
					if(userLand[userNum-1][j] == null) {
						break;
					}
				}
				return;
			}
		}
	}
	
	public static void landSell(int pNum) { // 땅 판매
		System.out.print("현재 " + pNum + "님이 보유중인 땅: ");
		for(int i=0; userLand[pNum-1][i] != null; i++) {
			System.out.print(userLand[pNum-1][i]+" ");
		}
		System.out.println();
		System.out.println("팔고 싶은 땅을 입력하세요");
		String sland=in.nextLine();
		for(int j=0; j<9; j++) {
			if(map[11][10-j][0].equals(sland)) {
				user[pNum-1][0] += Integer.parseInt(map[11][10-j][1]);
				map[11][10-j][2]=" ";
			}
		}
		for(int j=0; j<9; j++) {
			 if(map[10-j][1][0].equals(sland)) {
				 user[pNum-1][0] += Integer.parseInt(map[10-j][1][1]);
				 map[10-j][1][2]=" ";
			 }
		}
		for(int j=0; j<9; j++) {
			 if(map[1][j+2][0].equals(sland)) {
				 user[pNum-1][0] += Integer.parseInt(map[1][j+2][1]);
				 map[1][j+2][2]=" ";
			 }
		}
		for(int j=0; j<9; j++) {
			 if(map[j+2][11][0].equals(sland)) {
				 user[pNum-1][0] += Integer.parseInt(map[j+2][11][1]);
				 map[j+2][11][2]=" ";
			 }
		}
		for(int i = 0;userLand[pNum-1][i] != null; i++) {
			if(userLand[pNum-1][i].equals(sland)) {
				subLand(pNum,sland);
			}
		}
	}
}

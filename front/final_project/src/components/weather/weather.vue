<template>
  <div>
    <p>{{ textContent }}</p>

    <h3>now</h3>
    <div>
      <ul>
        <li>{{ address }}</li> <br />
        <li>{{ nowTmp }}℃</li> <br />
        <li>{{ nowSky }}</li> <br />
        <li>{{ nowPop }}</li> <br />
        <li>H: {{ todayTmx }}° L: {{ todayTmn }}°</li>
      </ul>
    </div>

    <h3>24HOURS</h3>
    <div>
      <table border="1">
        <tr>
          <td v-for="eachTime in everyTime" :key="eachTime.fcstTime">
            {{ eachTime.fcstTime }}<br />
            {{ eachTime.sky }}<br />
            {{ eachTime.pop }}<br />
            {{ eachTime.tmp }}
          </td>
        </tr>
      </table>
    </div>

    <!-- 옷 추천~~ -->
    <h3>기온별 옷차림</h3>
    <div>
      {{ recommend }} <br />
      {{ message }}

      <!-- 태그별 옷추천 -->
      <div v-if="showRecom">

        <!-- v-for) subtags 배열을 반복하면서 각 subtag에 대한 작업을 수행
        v-bind:value와 v-bind:key는 subtag 값을 해당 HTML 요소의 속성에 바인딩합니다. 
        이렇게 하면 각 subtag에 대해 고유한 값을 가지게 됩니다. -->
        <div v-for="subtag in subtags" v-bind:value="subtag" v-bind:key="subtag">
          <br />
          <h5>{{ subtag }}</h5>
          <div v-if="naverList[subtag] != null">
            {{ message2 }} <br />
            <b-button @click="shopping(subtag)">{{ subtag }} 쇼핑하러 가기</b-button>
          </div>
          <div>
            <!-- :key="subtag"는 Vue의 key 속성을 사용하여 "각 그룹"을 고유하게 식별 => subtag 별 그룹 생성 -->
            <b-card-group deck :key="subtag"
              style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
              <!-- v-for) additionalCloset[subtag] 배열을 반복하면서 각 cloth에 대한 작업을 수행 -->
              <b-card v-for="cloth in additionalCloset[subtag]" :key="cloth.closetnum"
                :img-src="'http://localhost:8081/closets/img/' + memnum + '/' + cloth.closetnum" overlay
                style="max-width:200px; height:auto; flex: 0 0 250px;" @click="detail(cloth.closetnum)"
                @mouseover="cursorChange">
                <template #footer>
                  <small class="text-muted">
                    {{ cloth.cloth }}<br />
                  </small>
                </template>
              </b-card>
            </b-card-group>
            <!-- <b-button v-on:click="moreBtn">더보기</b-button> -->
          </div>

          <!-- 태그에 옷이 없을 때 띄울 네이버 쇼핑 링크 -->
          <div>
            <b-card-group deck :key="subtag"
              style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
              <b-card v-for="(dto, i) in naverList[subtag]" :key="i" :img-src="dto.img" overlay
                style="max-width:200px; height: auto; flex: 0 0 250px;" @click="shoppingLink(subtag, i)"
                @mouseover="cursorChange">
                <template #footer>
                  <small class="text-muted">
                    {{ dto.price }}원<br />
                  </small>
                </template>
              </b-card>
            </b-card-group>
          </div>
        </div>

      </div>

    </div>

  </div>
</template>

<script>


export default {
  name: 'TodayWeather',
  data() {
    return {
      textContent: '',
      latitude: '',
      longitude: '',
      x: '', // latitude를 변환한 x좌표
      y: '', // longitude를 변환한 y좌표
      today: '', // 오늘 날짜 20230624
      yesterday: '', // 어제 날짜 20230623
      tomorrow: '', // 내일 날짜 20230625
      nowTime: '', // 현재 시간 1600
      today_sunrise: '', // 오늘 일출
      today_sunset: '', // 오늘 일몰
      tomorrow_sunrise: '', // 내일 일출
      tomorrow_sunset: '', // 내일 일몰
      everyTime: [], // 오늘0000~내일2300까지의 정보객체를 담은 배열
      // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      address: '', // 주소명
      nowTmp: '', // 1시간 기온 
      nowSky: '', // 하늘 상태
      nowPty: '', // 강수 형태 -- 안씀~~
      nowPop: '', // 강수확률
      todayTmn: '', // 오늘 최저기온
      todayTmx: '', // 오늘 최고기온
      // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      memnum: sessionStorage.getItem("memnum"),
      recommend: '...로딩중 *^^*',
      message: '',
      message2: '옷장이 비었네요!',
      showRecom: false,
      //currentPage: 1
      subtags: [],
      /*
      additionalCloset을 []로 데이터 속성으로 선언
      -> Vue의 반응적인(reactive) 데이터 시스템에 의해 관리되는 배열
      --> JavaScript 객체처럼 사용하여 동적으로 속성을 추가하고 값을 할당할 수 있음!!
      => this.additionalCloset[sub] :  additionalCloset 객체에 sub라는 속성을 동적으로 추가
      추가된 속성은 Vue에 의해 반응적으로 추적되지는 않지만, 반복문 등을 통해 해당 속성을 접근하여 사용할 수 있음~

      즉, additionalCloset 객체는 Vue의 반응성과는 별개로 동작하는 일반적인 JavaScript 객체이며, 
      동적으로 속성을 추가하고 값을 할당할 수 있는 특징을 가지고 있음~~
      */
      additionalCloset: [], // 여기에 사실상 additionalCloset['반팔'], additionalCloset['셔츠'] .. 가 다 들어있는거임!
      naverList: []
    }
  },
  created() {
    this.getDate()
    if (!("geolocation" in navigator)) {
      this.textContent = 'Geolocation is not available.';
      return;
    }
    this.textContent = 'Locating...';

    // Get position
    navigator.geolocation.getCurrentPosition(async pos => {
      this.latitude = pos.coords.latitude;
      this.longitude = pos.coords.longitude;
      this.textContent = 'Your location data is ' + this.latitude + ', ' + this.longitude;
      this.dfsXyConv(this.latitude, this.longitude)
      this.toAddress()
      this.sunTime(this.today)
      this.sunTime(this.tomorrow)
      this.weather(this.recommendation) // 파라메터로 메서드도 넣을 수 있다구~~
    }, err => {
      console.log(err);
      this.textContent = err.message;
      this.latitude = '37.582425';
      this.longitude = '126.983977777777';
      this.dfsXyConv(this.latitude, this.longitude)
      this.toAddress()
      this.sunTime(this.today)
      this.sunTime(this.tomorrow)
      this.weather(this.recommendation)
    });

    // 현재 로그인 상태 확인.
    let token = sessionStorage.getItem('token');
    if (this.memnum == null) {
      this.message = "로그인 후 옷장에서 옷 추천을 받아보세요!";
      this.showRecom = false;
    } else {
      this.$axios.get(`http://localhost:8081/members/${this.memnum}`,
        { headers: { 'token': token } })
        .then(async res => {
          if (res.status == 200) {
            this.showRecom = !this.showRecom;
          } else {
            alert("오류 발생으로 인한 로그인 정보 불러오기 실패")
          }
        })
    }

  },
  methods: {
    // 온도 별 옷추천 및 소분류로 옷장 옷 검색 (전체 중 5개만)
    // 동기 함수로 하게되면 데이터가 완전히 로드되기 전에 b-card-group 및 b-card 요소가 렌더링되므로 원하는 리스트가 생성되지 않습니다.
    // async -> 비동기 함수 정의. 
    async recommendation() {
      var tmp = this.nowTmp;
      let recommend = '';
      let subtags = [];
      if (tmp >= 28) { // 28도 이상
        recommend = '반팔, 민소매, 반바지, 치마'
        subtags = ['반팔', '민소매', '반바지', '치마']

      } else if (23 <= tmp && tmp <= 27) { // 23~27도
        recommend = '반팔, 얇은셔츠, 반바지, 면바지'
        subtags = ['반팔', '셔츠', '반바지', '면바지']

      } else if (20 <= tmp && tmp <= 22) { // 20~22도
        recommend = '얇은가디건, 긴팔티, 면바지, 청바지'
        subtags = ['가디건', '긴팔티', '면바지', '청바지']

      } else if (17 <= tmp && tmp <= 19) { // 17~19도
        recommend = '얇은니트, 가디건, 맨투맨, 얇은재킷, 면바지, 청바지'
        subtags = ['니트', '가디건', '맨투맨', '자켓', '면바지', '청바지']

      } else if (12 <= tmp && tmp <= 16) { // 12~16도
        recommend = '자켓, 가디건, 야상, 맨투맨, 니트, 스타킹, 청바지, 면바지'
        subtags = ['자켓', '가디건', '야상', '맨투맨', '니트', '스타킹', '청바지', '면바지']

      } else if (9 <= tmp && tmp <= 11) { // 9~11도
        recommend = '자켓, 트렌치코트, 야상, 니트, 스타킹, 청바지, 면바지'
        subtags = ['자켓', '트렌치코트', '야상', '니트', '스타킹', '청바지', '면바지']

      } else if (5 <= tmp && tmp <= 8) { // 5~8도
        recommend = '코트, 히트텍, 니트, 청바지, 레깅스'
        subtags = ['코트', '히트텍', '니트', '청바지', '레깅스']

      } else { // 4도 이하
        recommend = '패딩, 두꺼운코트, 목도리, 기모제품'
        subtags = ['패딩', '코트', '목도리', '기모제품']
      }
      this.recommend = recommend;
      this.subtags = subtags;

      // 소분류로 옷장 옷 검색 (전체 중 5개만)
      for (let i = 0; i < this.subtags.length; i++) {
        let subtag = this.subtags[i];
        try {
          // await -> 비동기 작업인 self.$axios.get(...)의 결과를 기다립니다.
          const res = await this.$axios.get('http://localhost:8081/closets/subtags/' + subtag);
          if (res.status === 200) {
            // 컴포넌트 처음 로딩될 때 옷장에서 999999999번 default(기본이미지) 걸러서 리스트에 넣기
            let closetlist = res.data.list.filter(closet => closet.closetnum !== 999999999);
            let addtionalRow = closetlist.slice(0, 5);
            // 배열에 subtag 속성 추가해서 검색 결과를 각각 담음.
            this.additionalCloset[subtag] = addtionalRow; // additionalCloset 객체에 속성으로 추가

            // 검색된 옷이 없으면 네이버 검색 띄워주기
            if (closetlist.length === 0) {
              const result = await this.$axios.get('http://localhost:8081/naver/' + subtag)
              if (result.status == 200) {
                let allList = result.data.list;
                let fivelist = allList.slice(0, 5);
                this.naverList[subtag] = fivelist;

                // for (let i = 0; i < self.naverList[subtag].length; i++) {
                //   let span = document.getElementById(i);
                //   span.innerHTML = self.naverList[i].title; // 불러오는 텍스트에 태그도 섞여있음!
                // }
                window.scrollTo({ top: 0, left: 0, behavior: 'smooth' });
              } else {
                alert("실패 시발!")
              }
            }
          } else {
            alert('에러코드: ' + res.status);
          }
        } catch (error) {
          alert('에러 발생: ' + error);
        }
      }

    },
    shopping(subtag) {
      let link = "https://search.shopping.naver.com/search/all?query=" + subtag;
      window.open(link);
    },
    shoppingLink(subtag, i) {
      let link = this.naverList[subtag][i].link;
      window.open(link, "_blank");
    },
    cursorChange(e) {
      e.target.style.cursor = "pointer";
    },
    // 위경도 좌표를 xy좌표로 변환하는 메서드
    dfsXyConv(v1, v2) {
      const { PI, tan, log, cos, pow, floor, sin } = Math
      //
      // LCC DFS 좌표변환을 위한 기초 자료
      //
      const RE = 6371.00877 // 지구 반경(km)
      const GRID = 5.0 // 격자 간격(km)
      const SLAT1 = 30.0 // 투영 위도1(degree)
      const SLAT2 = 60.0 // 투영 위도2(degree)
      const OLON = 126.0 // 기준점 경도(degree)
      const OLAT = 38.0 // 기준점 위도(degree)
      const XO = 43 // 기준점 X좌표(GRID)
      const YO = 136 // 기1준점 Y좌표(GRID)

      const DEGRAD = PI / 180.0
      //const RADDEG = 180.0 / PI

      const re = RE / GRID
      const slat1 = SLAT1 * DEGRAD
      const slat2 = SLAT2 * DEGRAD
      const olon = OLON * DEGRAD
      const olat = OLAT * DEGRAD

      let sn = tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5)
      sn = log(cos(slat1) / cos(slat2)) / log(sn)
      let sf = tan(PI * 0.25 + slat1 * 0.5)
      sf = pow(sf, sn) * cos(slat1) / sn
      let ro = tan(PI * 0.25 + olat * 0.5)
      ro = re * sf / pow(ro, sn)
      const rs = {}
      let ra, theta

      // 위경도 -> x, y좌표 
      rs.lat = v1
      rs.lon = v2
      ra = tan(PI * 0.25 + (v1) * DEGRAD * 0.5)
      ra = re * sf / pow(ra, sn)
      theta = v2 * DEGRAD - olon
      if (theta > PI) theta -= 2.0 * PI
      if (theta < -PI) theta += 2.0 * PI
      theta *= sn
      rs.x = floor(ra * sin(theta) + XO + 0.5)
      rs.y = floor(ro - ra * cos(theta) + YO + 0.5)

      this.x = rs.x
      this.y = rs.y
    },
    // 날짜 및 시간 구하는 메서드
    getDate() {
      var d = new Date(); // 현재 날짜 및 시간 구하는 메서드
      var year = d.getFullYear();
      //getMonth(), getDate(), getHours(), getMinutes(), getSeconds() 함수는 수치 값을 반환하기 때문에 2자리를 맞추기 위해서는 "0"을 붙여서 뒤에서 2자리만 잘라서 값을 변환해야 한다.
      var month = ('0' + (d.getMonth() + 1)).slice(-2); //getMonth()함수는 0~11을 반환하기 때문에 항상 +1을 해줘야한다
      var to_day = ('0' + d.getDate()).slice(-2);
      var yes_day = ('0' + (d.getDate() - 1)).slice(-2);
      var tomo_day = ('0' + (d.getDate() + 1)).slice(-2);
      var hours = ('0' + d.getHours()).slice(-2);

      this.today = year + month + to_day;
      this.yesterday = year + month + yes_day;
      this.tomorrow = year + month + tomo_day;
      this.nowTime = hours + '00';
    },
    // 시간별 날씨 구하는 메서드 
    weather(method) {
      const self = this;
      var apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
      var serviceKey = "Xs3HgrDkm6MWCtXs7NMgjrav%2By3qQRPpajwpJi45ktD6E8oGeF5HSc7WXJK3%2Fx43dBH1j7qRCv66dIkTP1JzyA%3D%3D";
      var pageNo = "1";
      var numOfRows = "580"; // 오늘 0000 ~ 내일 2300 
      var dataType = "JSON";
      var base_date = self.yesterday;
      var base_time = "2300";
      var nx = self.x;
      var ny = self.y;

      self.$axios.get(apiUrl + "?serviceKey=" + serviceKey + "&pageNo=" + pageNo + "&numOfRows=" + numOfRows + "&dataType=" + dataType + "&base_date=" + base_date + "&base_time=" + base_time + "&nx=" + nx + "&ny=" + ny)
        .then(res => {
          //console.log(res.data);
          if (res.data) {
            let weatherList = res.data.response.body.items.item; // Object 타입임
            if (weatherList != null) {
              let fcstTime = '';
              let eachTime = null; // 하루치 정보값을 담을 dto 
              //let wList = JSON.stringify(weatherList); // Object타입은 for문 안돼서 String 타입으로 바꿔주기
              //console.log(wList)
              for (const element of weatherList) { // 하루치 전체 배열 길이만큼 반복하며 객체 하나씩 꺼내기 
                let fcstDate = element.fcstDate;
                let temp = element.fcstTime; // 현재 객체의 예보 시간 값 담기
                if (fcstTime != temp) { // fcstTime 중복이 아닐경우 (다른 시간대일 경우)
                  if (eachTime !== null) { // 빈 dto(첫 dto) 거르기.
                    // push: 배열의 끝에 하나 또는 그 이상의 엘리먼트를 추가하고 배열의 변경된 길이(length)를 return
                    this.everyTime.push(eachTime); // dto에 새로운 dto를 할당하기 전에 완성된 dto를 list에 add한다.
                  }
                  eachTime = { fcstDate: '', fcstTime: '', sky: 0, pty: 0, pop: '', tmp: 0 }; // dto를 새로 만들어준다.
                  eachTime.fcstDate = fcstDate;
                  fcstTime = temp;
                  eachTime.fcstTime = fcstTime;
                  if (this.today == fcstDate && this.nowTime == temp) {
                    eachTime.fcstTime = 'now';
                  }
                }
                let category = element.category;
                let fcstValue = element.fcstValue;

                if (category == 'TMP') {
                  eachTime.tmp = fcstValue + '°';
                  if (this.today == fcstDate && this.nowTime == temp) {
                    this.nowTmp = fcstValue;
                  }
                } else if (category == 'SKY') {
                  eachTime.sky = fcstValue;
                  if (this.today == fcstDate && this.nowTime == temp) {
                    this.nowSky = fcstValue;
                  }
                } else if (category == 'PTY' && fcstValue != 0) {
                  eachTime.pty = +fcstValue + 4;
                  if (this.today == fcstDate && this.nowTime == temp) {
                    this.nowPty = +fcstValue + 4;
                  }
                } else if (category == 'POP') {
                  let pop = fcstValue + '%';
                  eachTime.pop = pop;
                  if (this.today == fcstDate && this.nowTime == temp) {
                    this.nowPop = pop;
                  }
                }

                // 0600은 tmn, 1500은 tmx가 추가로 들어있음.
                if (temp == '0600') {
                  if (this.today == fcstDate) {
                    this.todayTmn = fcstValue;
                  }
                }
                if (temp == '1500') {
                  if (this.today == fcstDate) {
                    this.todayTmx = fcstValue;
                  }
                }

              }
              this.everyTime.push(eachTime); // 마지막 2300 dto 담기

              // filter(): 새로운 배열을 반환하며, 조건을 충족하는 요소만 남김.
              // 오늘 0000 ~ 내일 2300 담겨있는 배열에서 
              // now ~ 내일 now 까지 필터링해서 담기 
              this.everyTime = this.everyTime.filter(eachTime => {
                if (eachTime.fcstTime === 'now') {
                  return true;
                } else if (eachTime.fcstDate === this.today && eachTime.fcstTime > this.nowTime) { // 오늘 + now 이후시간
                  return true;
                } else if (eachTime.fcstDate != this.today && eachTime.fcstTime <= this.nowTime) { // 내일 + now 이전시간
                  return true;
                }
                return false;
              });


              // 날씨 배열에 끼워넣을 일출일몰 객체 
              let todaySunriseObject = { fcstDate: this.today, fcstTime: this.today_sunrise, sky: 200, pty: '', pop: '', tmp: 'sunrise' };
              let todaySunsetObject = { fcstDate: this.today, fcstTime: this.today_sunset, sky: 100, pty: '', pop: '', tmp: 'sunset' };
              let tomorrowSunriseObject = { fcstDate: this.tomorrow, fcstTime: this.tomorrow_sunrise, sky: 200, pty: '', pop: '', tmp: 'sunrise' };
              let tomorrowSunsetObject = { fcstDate: this.tomorrow, fcstTime: this.tomorrow_sunset, sky: 100, pty: '', pop: '', tmp: 'sunset' };

              // 조건별로 일출일몰 객체 다르게 끼워넣기~
              // (1) now = 0000 ~ 일출
              // (2) now = 일출 ~ 일몰
              // (3) now = 일몰 ~ 0000
              if (this.nowTime.substring(0, 2) <= this.today_sunrise.substring(0, 2)) {
                // (1) 막 하루가 지났을 때 => now(0000) ..(밤).. 오늘 일출 ... 오늘 일몰 ..(밤).. (0000)
                // findIndex(): 조건에 맞는 첫 번째 요소의 인덱스 값을 반환. 없으면 -1.
                let todaySR = this.everyTime.findIndex((eachTime) => eachTime.fcstTime.substring(0, 2) === this.today_sunrise.substring(0, 2));
                let todaySS = this.everyTime.findIndex((eachTime) => eachTime.fcstTime.substring(0, 2) === this.today_sunset.substring(0, 2));
                // 일출, 일몰 기준으로 배열 자르기~~
                let first = this.everyTime.slice(0, todaySR + 1);
                let second = this.everyTime.slice(todaySR + 1, todaySS + 1);
                let third = this.everyTime.slice(todaySS + 1, this.everyTime.length);

                for (const eachTime of first) { // now ~ 일출 (밤)
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }
                for (const eachTime of third) { // 일몰 ~ (밤)
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }
                // 일출일몰 껴서 배열 다시 생성~
                first.push(todaySunriseObject); // 오늘 일출 추가
                second.push(todaySunsetObject); // 오늘 일몰 추가
                this.everyTime = first.concat(second, third);

              } else if (this.nowTime.substring(0, 2) <= this.today_sunset.substring(0, 2)) {
                // (2) 해는 떴지만 아직 지지 않았을 때 => now(0700) ... 오늘 일몰 ..(밤).. 내일 일출 ... (0700)
                let todaySS = this.everyTime.findIndex((eachTime) => eachTime.fcstTime.substring(0, 2) === this.today_sunset.substring(0, 2));
                let tomorrowSR = this.everyTime.findIndex((eachTime) => eachTime.fcstTime.substring(0, 2) === this.tomorrow_sunrise.substring(0, 2));

                let first = this.everyTime.slice(0, todaySS + 1);
                let second = this.everyTime.slice(todaySS + 1, tomorrowSR + 1);
                let third = this.everyTime.slice(tomorrowSR + 1, this.everyTime.length);

                for (const eachTime of second) { // 일몰 ~ 일출 (밤)
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }

                first.push(todaySunsetObject); // 오늘 일몰 추가
                second.push(tomorrowSunriseObject); // 내일 일출 추가
                this.everyTime = first.concat(second, third);

              } else {
                // (3) 해가 졌을 때 => now(2100) ..(밤).. 내일 일출 ... 내일 일몰 ..(밤).. (2100)
                let tomorrowSR = this.everyTime.findIndex((eachTime) => eachTime.fcstTime.substring(0, 2) === this.tomorrow_sunrise.substring(0, 2));
                let tomorrowSS = this.everyTime.findIndex((eachTime) => eachTime.fcstTime.substring(0, 2) === this.tomorrow_sunset.substring(0, 2));

                let first = this.everyTime.slice(0, tomorrowSR + 1);
                let second = this.everyTime.slice(tomorrowSR + 1, tomorrowSS + 1);
                let third = this.everyTime.slice(tomorrowSS + 1, this.everyTime.length);

                for (const eachTime of first) { // now ~ 일출 (밤)
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }
                for (const eachTime of third) { // 일몰 ~ (밤)
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }
                first.push(tomorrowSunriseObject); // 내일 일출 추가
                second.push(tomorrowSunsetObject); // 내일 일몰 추가
                this.everyTime = first.concat(second, third);
              }

              // 하늘상태와 강수형태가 섞이지 않게 하기~
              for (const eachTime of this.everyTime) { // 날씨 리스트
                if (eachTime.pty != 0) { // 비가 내리면~
                  eachTime.sky = eachTime.pty; // sky에 값 몰아주기. pty는 안쓸변수임
                  eachTime.pty = '';
                } else { // 비 안오면 sky 제외한 변수값 초기화
                  eachTime.pty = '';
                  eachTime.pop = '';
                }
              }
              if (this.nowPty != 0) { // 현재
                this.nowSky = this.nowPty;
                this.nowPty = '';
              }
              // 옷 추천하는 메서드 (recommendation())호출하는 메서드
              method();
            } else {
              console.log('에러코드:', res.status);
            }
          } else {
            console.log('데이터 없음');
          }
        })
        .catch(err => {
          console.log(err);
          this.textContent = err.message;
        });
    },
    // 위경도 값에 따른 주소명 구하는 메서드 
    toAddress() {
      var REST_API_KEY = "KakaoAK cf647b246dec58429d3ed126a442d1e0"
      const self = this;
      self.$axios.get('https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=' + self.longitude + '&y=' + self.latitude, { headers: { 'Authorization': REST_API_KEY } })
        .then(res => {
          if (res.data) {
            var totalCount = res.data.meta.total_count; //총 문서 수
            if (totalCount > 0) {
              this.address = res.data.documents[0].region_2depth_name; //지역 2Depth, 구 단위
            } else {
              console.log('에러코드:', res.status);
            }
          } else {
            console.log('데이터 없음');
          }
        })
        .catch(err => {
          console.log(err);
          this.textContent = err.message;
        });
    },
    // 일출 일몰 구하는 메서드
    sunTime(day) {
      const self = this;
      var apiUrl = "http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo";
      var longitude = self.longitude;
      var latitude = self.latitude;
      var locdate = day;
      var dnYn = "Y";
      var serviceKey = "Xs3HgrDkm6MWCtXs7NMgjrav%2By3qQRPpajwpJi45ktD6E8oGeF5HSc7WXJK3%2Fx43dBH1j7qRCv66dIkTP1JzyA%3D%3D";

      self.$axios.get(apiUrl + "?longitude=" + longitude + "&latitude=" + latitude + "&locdate=" + locdate + "&dnYn=" + dnYn + "&serviceKey=" + serviceKey)
        .then(res => {
          if (res.data) {
            // xml 응답값을 json으로 변환
            let xmlData = res.data;
            let jsonData = JSON.stringify(xmlData); // xml -> json 형태의 String
            let objectData = JSON.parse(jsonData); // json 형태의 String -> object 타입의 json
            if (locdate === this.today) {
              this.today_sunrise = objectData.response.body.items.item.sunrise;
              this.today_sunset = objectData.response.body.items.item.sunset;
            } else if (locdate === this.tomorrow) {
              this.tomorrow_sunrise = objectData.response.body.items.item.sunrise;
              this.tomorrow_sunset = objectData.response.body.items.item.sunset;
            }
          } else {
            console.log('데이터 없음');
          }
        })
        .catch(err => {
          console.log(err);
          this.textContent = err.message;
        });
    },
    detail(closetnum) {
      this.$router.push({ name: 'ClosetDetail', query: { closetnum: closetnum } })
    }
  }
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Pacifico&display=swap");

img {
  width: 100%;
  height: 100%;
}


h3 {
  margin: 40px 0 0;
}


table {
  margin-left: auto;
  margin-right: auto;
}

td {
  margin-left: 5px;
  margin-right: 5px
}

ul {
  list-style-type: none;
  padding: 0;
  /* margin-left: 27%; */
  cursor: pointer;
}

li {
  /* display: inline-block; */
  /* margin: 0 10px; */
  list-style: none;
  /* float: left; */
  /* margin-right: 80px; */
  cursor: pointer;
}

.follow {
  clear: left;
}
</style>
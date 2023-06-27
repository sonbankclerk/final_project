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
        <li>H: {{ todayTmx }}℃ L: {{ todayTmn }}℃</li>
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
      {{ recommend }}
      {{ message }}

      <div v-if="showRecom">
        
        <b-card-group deck v-for="(row, index) in additionalCloset" :key="index" style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
          <b-card v-for="closet in row" :key="closet.closetnum"
            :img-src="'http://localhost:8081/closets/img/' + memnum + '/' + closet.closetnum" img-top
            style="max-width:200px; height: auto; flex: 0 0 250px;">
            <b-card-text>
              {{ closet.maintag }}<br />
              {{ closet.subtag }}<br />
              <a v-on:click="detail(closet.closetnum)">{{ closet.cloth }}</a><br />
            </b-card-text>
            <template #footer>
              <small class="text-muted">
                <b-button v-on:click="deletecloth(closet.closetnum)">삭제</b-button>
              </small>
            </template>
          </b-card>
        </b-card-group>
        <b-button v-on:click="moreBtn">더보기</b-button>

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
      showRecom: false,
      //currentPage: 1
      subtags: [],
      additionalCloset: []
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
    navigator.geolocation.getCurrentPosition(pos => {
      this.latitude = pos.coords.latitude;
      this.longitude = pos.coords.longitude;
      this.textContent = 'Your location data is ' + this.latitude + ', ' + this.longitude;
      this.dfsXyConv(this.latitude, this.longitude)
      this.toAddress()
      this.sunTime(this.today)
      this.sunTime(this.tomorrow)
      this.weather(this.recommendation) // 파라메터로 메서드도 넣을 수 있다구~~
      for(let i=0; i<this.subtags.length; i++){
        let sub = this.subtags[i]
        this.getList(sub)
      }
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
      .then(res => {
        if (res.status == 200) {
          this.showRecom = !this.showRecom;
          for(let i=0; i<this.subtags.length; i++){
            let sub = this.subtags[i]
            this.getList(sub)
          }
        } else {
            alert("오류 발생으로 인한 로그인 정보 불러오기 실패")
          }
        })
    }

  },
  methods: {
    // 소분류로 옷장 옷 검색 (전체 중 5개만)
    getList(sub) {
      const self = this;
      self.memnum = sessionStorage.getItem('memnum')
      self.$axios.get('http://localhost:8081/closets/subtags/' + sub)
        .then(function (res) {
          if (res.status == 200) {
            // 컴포넌트 처음 로딩될 때 옷장에서 999999999번 default(기본이미지) 걸러서 리스트에 넣기
            let closetlist = res.data.list.filter(closet => closet.closetnum != 999999999);
            let addtionalRow = closetlist.slice(0, 5);
            self.additionalCloset.push(addtionalRow);
          } else {
            alert('에러코드: ' + res.status)
          }
        })
    },
    // 온도 별 옷추천~
    recommendation() {
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
                  eachTime.tmp = fcstValue + '℃';
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
              // 24시간 날씨 배열에 끼워넣을 일출일몰 객체 
              let todaySunset = { fcstDate: this.today, fcstTime: this.today_sunset, sky: 100, pty: '', pop: '', tmp: 'sunset' };
              let tomorrowSunrise = { fcstDate: this.tomorrow, fcstTime: this.tomorrow_sunrise, sky: 200, pty: '', pop: '', tmp: 'sunrise' };
              let tomorrowSunset = { fcstDate: this.tomorrow, fcstTime: this.tomorrow_sunset, sky: 100, pty: '', pop: '', tmp: 'sunset' };

              // 일몰 시간이 아직 지나지 않았을 때
              if (this.nowTime.substring(0, 2) <= this.today_sunset.substring(0, 2)) {
                // findIndex(): 조건에 맞는 첫 번째 요소의 인덱스 값을 반환. 없으면 -1.
                let tdsunset_index = this.everyTime.findIndex((eachTime) => eachTime.fcstTime.substring(0, 2) === this.today_sunset.substring(0, 2));
                let tmsunrise_index = this.everyTime.findIndex((eachTime) => eachTime.fcstTime.substring(0, 2) === this.tomorrow_sunrise.substring(0, 2));
                // 일출, 일몰 기준으로 배열 자르기~~
                let first = this.everyTime.slice(0, tdsunset_index + 1);
                let second = this.everyTime.slice(tdsunset_index + 1, tmsunrise_index + 1);
                let third = this.everyTime.slice(tmsunrise_index + 1, this.everyTime.length);

                for (const eachTime of second) { // 밤에는 밤 아이콘을 띄워줘야함~
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }
                // 일출일몰 껴서 배열 다시 생성~
                first.push(todaySunset);
                second.push(tomorrowSunrise);
                this.everyTime = first.concat(second, third);

              } else { // 일몰시간이 지났을 때~~~~~~~~~~~~
                let tmsunrise_index = this.everyTime.findIndex((eachTime) => eachTime.fcstTime.substring(0, 2) === this.tomorrow_sunrise.substring(0, 2));
                let tmsunset_index = this.everyTime.findIndex((eachTime) => eachTime.fcstTime.substring(0, 2) === this.tomorrow_sunset.substring(0, 2));

                let first = this.everyTime.slice(0, tmsunrise_index + 1);
                let second = this.everyTime.slice(tmsunrise_index + 1, tmsunset_index + 1);
                let third = this.everyTime.slice(tmsunset_index + 1, this.everyTime.length);

                for (const eachTime of first) { // 밤에는 밤 아이콘을 띄워줘야함~
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }
                for (const eachTime of third) { // 밤에는 밤 아이콘을 띄워줘야함~
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }
                first.push(tomorrowSunrise);
                second.push(tomorrowSunset);
                this.everyTime = first.concat(second, third);
              }

              for (const eachTime of this.everyTime) { // 하늘상태와 강수형태가 섞이지 않게 하기~
                if (eachTime.pty != 0) { // 비가 내리면~
                  eachTime.sky = eachTime.pty; // sky에 값 몰아주기. pty는 안쓸변수임
                  eachTime.pty = '';
                } else { // 비 안오면 sky 제외한 변수값 초기화
                  eachTime.pty = '';
                  eachTime.pop = '';
                }
              }

              if (this.nowPty != 0) {
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
h3 {
  margin: 40px 0 0;
}

.star {
  margin-left: 150px;
  margin-top: 1000px;
  cursor: pointer;
}

table {
  margin-left: auto;
  margin-right: auto;
}

ul {
  margin-left: 27%;
  cursor: pointer;
}

li {
  list-style: none;
  float: left;
  margin-right: 80px;
  cursor: pointer;
}

.follow {
  clear: left;
}
</style>
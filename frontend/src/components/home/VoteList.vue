<template>
  <div class="modu">
    <div class="box-row-left">
      <div class="text-title text-h1">모두에게 물어봐</div>
      <select
        v-model="category"
        class="sb-rectangle-big"
        :class="{
          'yellow-0': category === '메뉴',
          'purple-0': category === '스타일',
          'green-0': category === '장소',
        }"
        @change="changeCategory()"
      >
        <option v-for="(item, index) in categoryList" :key="index">
          {{ item }}
        </option>
      </select>
    </div>
    <div class="box-row-left">
      <div
        class="text-title text-h4"
        :class="{
          'yellow-3-text': category === '메뉴',
          'purple-3-text': category === '스타일',
          'green-3-text': category === '장소',
        }"
      >
        사람들과 고민을 공유해보세요 !
      </div>
    </div>
    <div class="box-column">
      <div class="box-row">
        <div
          class="text-h2"
          :class="{
            'yellow-3-text': category === '메뉴',
            'purple-3-text': category === '스타일',
            'green-3-text': category === '장소',
          }"
        >
          인기 투표🔥
        </div>
      </div>

      <div class="box-row">
        <slider-chart :category="category" :key="reload" />
      </div>
    </div>
    <div class="box-column">
      <div class="box-row">
        <div
          class="text-h2"
          :class="{
            'yellow-3-text': category === '메뉴',
            'purple-3-text': category === '스타일',
            'green-3-text': category === '장소',
          }"
        >
          투표 목록💌
        </div>
        <div
          class="btn-rectangle-small"
          :class="{
            'yellow-2': category === '메뉴',
            'purple-2': category === '스타일',
            'green-2': category === '장소',
          }"
          @click="createVote"
        >
          <div class="text-align-center">만들기</div>
        </div>
      </div>
      <div class="box-btn-right">
        <!-- <control-view-2 :segments="status" class="toggle-btn" /> -->
        <select
          v-model="status"
          class="sb-rectangle-small text-h4"
          :class="{
            'yellow-1': category === '메뉴' && status === '진행',
            'purple-1': category === '스타일' && status === '진행',
            'green-1': category === '장소' && status === '진행',
            'yellow-0': category === '메뉴' && status === '종료',
            'purple-0': category === '스타일' && status === '종료',
            'green-0': category === '장소' && status === '종료',
          }"
          @change="changeStatus"
        >
          <option v-for="(item, index) in statusList" :key="index">
            {{ item }}
          </option>
        </select>
      </div>

      <div class="vote-list">
        <div
          class="vote-card"
          v-for="(vote, index) in vote_list"
          :key="index"
          @click="detailCard"
          :value="`${vote.voteId}`"
          :style="{
            backgroundImage: `linear-gradient(
                rgba(255, 255, 255, 0.5),
                rgba(255, 255, 255, 0.5)
                ), url(${require('@/assets/image/category/' +
                  vote.category +
                  '/' +
                  vote.subCategory.replace('/', '_') +
                  '.jpg')})`,
          }"
        >
          <div
            class="vote-title-box"
            @click="detailCard"
            :value="`${vote.voteId}`"
          >
            <div
              class="vote-title-text text-h3"
              @click="detailCard"
              :value="`${vote.voteId}`"
            >
              {{ vote.title }}
            </div>
          </div>
          <div
            class="vote-writer-box"
            @click="detailCard"
            :value="`${vote.voteId}`"
          >
            <div
              class="vote-writer-text text-h4"
              @click="detailCard"
              :value="`${vote.voteId}`"
            >
              작성자 : {{ vote.name }}
            </div>
          </div>
          <div
            class="vote-options-box"
            @click="detailCard"
            :value="`${vote.voteId}`"
          >
            <div
              class="vote-option-box"
              :class="{
                'yellow-2-border': category === '메뉴',
                'purple-2-border': category === '스타일',
                'green-2-border': category === '장소',
              }"
              @click="detailCard"
              :value="`${vote.voteId}`"
            >
              <div
                class="vote-option-text text-h4"
                @click="detailCard"
                :value="`${vote.voteId}`"
              >
                {{ vote.optionA }}
              </div>
            </div>
            <div class="vote-option-vs">vs</div>
            <div
              class="vote-option-box"
              :class="{
                'yellow-2-border': category === '메뉴',
                'purple-2-border': category === '스타일',
                'green-2-border': category === '장소',
              }"
              @click="detailCard"
              :value="`${vote.voteId}`"
            >
              <div
                class="vote-option-text text-h4"
                @click="detailCard"
                :value="`${vote.voteId}`"
              >
                {{ vote.optionB }}
              </div>
            </div>
          </div>
          <div
            class="vote-info-box"
            @click="detailCard"
            :value="`${vote.voteId}`"
          >
            <div
              class="vote-category-box"
              @click="detailCard"
              :value="`${vote.voteId}`"
            >
              <div
                class="vote-category-main text-h5"
                @click="detailCard"
                :value="`${vote.voteId}`"
              >
                # {{ vote.category }}
              </div>
              <div
                class="vote-category-sub text-h5"
                @click="detailCard"
                :value="`${vote.voteId}`"
                id="sub-category"
              >
                # {{ vote.subCategory }}
              </div>
            </div>
            <div
              class="vote-count-box"
              @click="detailCard"
              :value="`${vote.voteId}`"
            >
              <div
                class="vote-view-box"
                @click="detailCard"
                :value="`${vote.voteId}`"
              >
                <img
                  src="../../assets/icon/views.png"
                  alt="조회"
                  class="vote-btn-view"
                />
                <div class="text-h5">
                  {{ vote.hitCount }}
                </div>
              </div>
              <div
                class="vote-comment-box"
                @click="detailCard"
                :value="`${vote.voteId}`"
              >
                <img
                  src="../../assets/icon/chat.png"
                  alt="댓글"
                  class="vote-btn-comment"
                />
                <div class="text-h5">
                  {{ vote.commentCount }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 여기였어 -->
    </div>
    <infinite-loading
      @infinite="infiniteHandler"
      spinner="waveDots"
      :key="reload"
    >
      <div
        slot="no-more"
        style="color: rgb(102, 102, 102); font-size: 14px; padding: 10px 0px"
      >
        목록의 끝입니다 :)
      </div>
    </infinite-loading>
  </div>
</template>

<script></script>

<script>
import { mapState, mapActions, mapMutations } from "vuex";
import InfiniteLoading from "vue-infinite-loading";
import SliderChart from "./SliderChart";
export default {
  name: "VoteList",
  components: {
    InfiniteLoading,
    SliderChart,
  },
  data() {
    return {
      status: "진행",
      booleanStatus: false,
      category: "메뉴",
      categoryList: ["메뉴", "스타일", "장소"],
      statusList: ["진행", "종료"],
      id: 0,
      reload: 0,
    };
  },
  computed: {
    ...mapState("communityStore", ["vote_list", "lastVoteId", "last"]),
  },
  methods: {
    ...mapActions("communityStore", ["SHOW_VOTE_LIST"]),
    ...mapMutations("communityStore", ["SET_INIT"]),
    createVote() {
      if (this.$store.state.userStore.userId === 0) {
        this.$router.push({ name: "userLogin" });
      } else this.$router.push({ name: "voteCreate" });
    },
    detailCard(e) {
      if (this.$store.state.userStore.userId === 0) {
        this.$router.push({ name: "userLogin" });
      } else {
        const clickedId = e.target.getAttribute("value");
        // 파라미터 같이 보내기 !
        this.$router.push({
          name: "voteDetail",
          params: {
            voteId: clickedId,
          },
        });
      }
    },
    set_init() {
      this.SET_INIT();
    },
    votes_list(params) {
      this.SHOW_VOTE_LIST({
        params: params,
      });
    },
    changeCategory() {
      this.reload += 1;
      let params = {
        category: this.category,
        status: false,
        lastVoteId: 0,
      };
      this.set_init();
      this.votes_list(params);
      this.$emit("pass", this.category);
      this.status = "진행";
      this.$router.push({
        name: "voteList",
        path: "/",
        params: { status: this.status, category: this.category },
      });
    },

    changeStatus() {
      this.reload += 1;
      // 여기서 진행 종료 바꾸는 목록 함수 호출
      this.set_init();
      //초기화
      if (this.status == "진행") {
        this.booleanStatus = false;
      } else {
        this.booleanStatus = true;
      }

      let params = {
        category: this.category,
        status: this.booleanStatus,
        lastVoteId: 0,
      };

      this.votes_list(params);
    },
    infiniteHandler($state) {
      if (this.status == "진행") {
        this.booleanStatus = false;
      } else {
        this.booleanStatus = true;
      }
      let params = {
        category: this.category,
        status: this.booleanStatus,
        lastVoteId: this.lastVoteId,
      };
      this.votes_list(params);
      setTimeout(() => {
        if (!this.last) {
          $state.loaded();
          if (this.last) {
            $state.complete();
          }
        } else {
          $state.complete();
        }
      }, 3000);
    },
  },

  mounted() {
    // voteList api 호출
    if (this.status == "진행") {
      this.booleanStatus = false;
    } else {
      this.booleanStatus = true;
    }
    var params = {
      category: this.category,
      status: this.booleanStatus,
      lastVoteId: 0,
    };
    this.set_init();
    this.votes_list(params);
  },
};
</script>

<style scoped></style>

(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-d61158f6"],{"0ac3":function(t,a,e){"use strict";e.r(a);var s=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("page-layout",{attrs:{avatar:t.previewAction(t.currUser.avatar)}},[e("div",{attrs:{slot:"headerContent"},slot:"headerContent"},[e("div",{staticClass:"title"},[t._v(t._s(t.welcome.timeFix)+"，"+t._s(t.currUser.username)+"，"+t._s(t.welcome.message))]),e("div",[t._v(t._s(t.currUser.roleNames.join(",")))])]),e("template",{slot:"extra"},[e("head-info",{staticClass:"split-right",attrs:{title:t.$t("project"),content:"56"}}),e("head-info",{staticClass:"split-right",attrs:{title:t.$t("ranking"),content:"8/24"}}),e("head-info",{staticClass:"split-right",attrs:{title:t.$t("visit"),content:"2,223"}})],1),[e("a-row",{staticStyle:{margin:"0 -12px"}},[e("a-col",{staticStyle:{padding:"0 12px"},attrs:{xl:16,lg:24,md:24,sm:24,xs:24}},[e("a-card",{staticClass:"project-list",staticStyle:{"margin-bottom":"24px"},attrs:{loading:t.loading,bordered:!1,title:t.$t("progress"),"body-style":{padding:0}}},[e("a",{attrs:{slot:"extra"},slot:"extra"},[t._v(t._s(t.$t("all")))]),e("div",t._l(t.projects,(function(a,s){return e("a-card-grid",{key:s},[e("a-card",{attrs:{bordered:!1,"body-style":{padding:0}}},[e("a-card-meta",{attrs:{description:a.desc}},[e("div",{staticClass:"card-title",attrs:{slot:"title"},slot:"title"},[e("a-avatar",{attrs:{size:"small",src:a.logo}}),e("span",[t._v("Alipay")])],1)]),e("div",{staticClass:"project-item"},[e("a",{staticClass:"group",attrs:{href:"/#/"}},[t._v("科学搬砖组")]),e("span",{staticClass:"datetime"},[t._v("9小时前")])])],1)],1)})),1)]),e("a-card",{attrs:{loading:t.loading,title:t.$t("dynamic"),bordered:!1}},[e("a-list",t._l(t.activities,(function(a,s){return e("a-list-item",{key:s},[e("a-list-item-meta",[e("a-avatar",{attrs:{slot:"avatar",src:a.user.avatar},slot:"avatar"}),e("div",{attrs:{slot:"title"},domProps:{innerHTML:t._s(a.template)},slot:"title"}),e("div",{attrs:{slot:"description"},slot:"description"},[t._v("9小时前")])],1)],1)})),1)],1)],1),e("a-col",{staticStyle:{padding:"0 12px"},attrs:{xl:8,lg:24,md:24,sm:24,xs:24}},[e("a-card",{staticStyle:{"margin-bottom":"24px"},attrs:{title:t.$t("access"),bordered:!1,"body-style":{padding:0}}},[e("div",{staticClass:"item-group"},[e("a",[t._v("操作一")]),e("a",[t._v("操作二")]),e("a",[t._v("操作三")]),e("a",[t._v("操作四")]),e("a",[t._v("操作五")]),e("a",[t._v("操作六")]),e("a-button",{attrs:{size:"small",type:"primary",ghost:"",icon:"plus"}},[t._v(t._s(t.$t("add")))])],1)]),e("a-card",{staticStyle:{"margin-bottom":"24px"},attrs:{loading:t.loading,title:"XX "+t.$t("degree"),bordered:!1,"body-style":{padding:0}}},[e("div",{staticStyle:{"min-height":"400px"}},[e("radar")],1)]),e("a-card",{attrs:{loading:t.loading,title:t.$t("team"),bordered:!1}},[e("div",{staticClass:"members"},[e("a-row",t._l(t.teams,(function(a,s){return e("a-col",{key:s,attrs:{span:12}},[e("a",[e("a-avatar",{attrs:{size:"small",src:a.avatar}}),e("span",{staticClass:"member"},[t._v(t._s(a.name))])],1)])})),1)],1)])],1)],1)]],2)},i=[],r=e("5530"),n=e("45eb"),c=e("779e"),l=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("v-chart",{attrs:{forceFit:!0,height:"400",data:t.data,padding:[20,20,95,20],scale:t.scale}},[e("v-tooltip"),e("v-axis",{attrs:{dataKey:t.axis1Opts.dataKey,line:t.axis1Opts.line,tickLine:t.axis1Opts.tickLine,grid:t.axis1Opts.grid}}),e("v-axis",{attrs:{dataKey:t.axis2Opts.dataKey,line:t.axis2Opts.line,tickLine:t.axis2Opts.tickLine,grid:t.axis2Opts.grid}}),e("v-legend",{attrs:{dataKey:"user",marker:"circle",offset:30}}),e("v-coord",{attrs:{type:"polar",radius:"0.8"}}),e("v-line",{attrs:{position:"item*score",color:"user",size:2}}),e("v-point",{attrs:{position:"item*score",color:"user",size:4,shape:"circle"}})],1)},o=[],d=e("40ba"),p=[{item:"引用",a:70,b:30,c:40},{item:"口碑",a:60,b:70,c:40},{item:"产量",a:50,b:60,c:40},{item:"贡献",a:40,b:50,c:40},{item:"热度",a:60,b:70,c:40},{item:"引用",a:70,b:50,c:40}],m=(new d.View).source(p);m.transform({type:"fold",fields:["a","b","c"],key:"user",value:"score"});var u=[{dataKey:"score",min:0,max:80}],v=m.rows,g={dataKey:"item",line:null,tickLine:null,grid:{lineStyle:{lineDash:null},hideFirstLine:!1}},b={dataKey:"score",line:null,tickLine:null,grid:{type:"polygon",lineStyle:{lineDash:null}}},y={name:"Radar",data:function(){return{sourceData:p,data:v,axis1Opts:g,axis2Opts:b,scale:u}}},_=y,f=e("0c7c"),x=Object(f["a"])(_,l,o,!1,null,"46c2ce9f",null),h=x.exports,k=e("5880"),O=e("b775"),j=e("7424"),w=e("7373"),C={name:"WorkPlace",components:{Radar:h,HeadInfo:c["a"],PageLayout:n["a"]},mixins:[w["a"]],data:function(){return{projects:[],loading:!0,activities:[],teams:[],welcome:{timeFix:"",message:""}}},computed:Object(r["a"])(Object(r["a"])({},Object(k["mapState"])("account",{currUser:"user"})),Object(k["mapState"])("setting",["lang"])),created:function(){var t=this;Object(O["e"])(j["BASE_URL"]+"/mock/user/welcome",O["a"].GET).then((function(a){return t.welcome=a.data})),Object(O["e"])(j["BASE_URL"]+"/mock/work/activity",O["a"].GET).then((function(a){return t.activities=a.data})),Object(O["e"])(j["BASE_URL"]+"/mock/work/team",O["a"].GET).then((function(a){return t.teams=a.data})),Object(O["e"])(j["BASE_URL"]+"/mock/project",O["a"].GET).then((function(a){t.projects=a.data,t.loading=!1}))}},S=C,L=(e("a7ca"),Object(f["a"])(S,s,i,!1,null,null,null)),$=L.exports;a["default"]=$},4266:function(t,a,e){},6627:function(t,a,e){"use strict";var s=e("4266"),i=e.n(s);i.a},"779e":function(t,a,e){"use strict";var s=function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticClass:"head-info"},[e("span",[t._v(t._s(t.title))]),e("p",[t._v(t._s(t.content))])])},i=[],r={name:"HeadInfo",props:["title","content","bordered"]},n=r,c=(e("6627"),e("0c7c")),l=Object(c["a"])(n,s,i,!1,null,"46a43dda",null);a["a"]=l.exports},"9bd1":function(t,a,e){},a7ca:function(t,a,e){"use strict";var s=e("9bd1"),i=e.n(s);i.a}}]);
// 获取 HTML 元素
function getElement(id) {
    const el = document.getElementById(id);
    if (!el) {
        throw new Error(`Element "cn.isqing.icloud:icloud-app-freeswitch:jar:1.0.0-SNAPSHOT" not found`);
    }
    return el;
}

// 获取 HTML 输入元素
function getInput(id) {
    const el = getElement(id);
    if (!(el instanceof HTMLInputElement)) {
        throw new Error(`Element "cn.isqing.icloud:icloud-app-freeswitch:jar:1.0.0-SNAPSHOT" not an input element`);
    }
    return el;
}

// 获取 HTML 按钮元素
function getButton(id) {
    const el = getElement(id);
    if (!(el instanceof HTMLButtonElement)) {
        throw new Error(`Element "cn.isqing.icloud:icloud-app-freeswitch:jar:1.0.0-SNAPSHOT" not a button element`);
    }
    return el;
}

// 获取 HTML 音频元素
function getAudio(id) {
    const el = getElement(id);
    if (!(el instanceof HTMLAudioElement)) {
        throw new Error(`Element "cn.isqing.icloud:icloud-app-freeswitch:jar:1.0.0-SNAPSHOT" not an audio element`);
    }
    return el;
}

// 获取 HTML span 元素
function getSpan(id) {
    const el = getElement(id);
    if (!(el instanceof HTMLSpanElement)) {
        throw new Error(`Element "cn.isqing.icloud:icloud-app-freeswitch:jar:1.0.0-SNAPSHOT" not a span element`);
    }
    return el;
}

// 创建媒体选项
const mediaOptions = {
    media: {
        constraints: {
            audio: true, // 是否开启音频
            video: false // 是否开启视频
        },
        render: {
            remote: getAudio("remoteAudio") // 远端音频元素
        }
    }
};

// 声明 SimpleUser 变量
let simpleUser;

getButton("connect").addEventListener("click", () => {
    // 使用输入框中的值创建一个新的 SimpleUser 实例
    simpleUser = new SIP.Web.SimpleUser(getInput("server").value, {
        aor: getInput("aor").value, // 分机号
        userAgentOptions: {
            sipExtension100rel: SIP.SIPExtension.Supported,
            sipExtensionReplaces: SIP.SIPExtension.Supported,
            sipExtensionExtraSupported: ["timer"],
            authorizationPassword: getInput("password").value,
        },
        sessionDescriptionHandlerFactoryOptions: {
            constraints: {audio: true, video: false},
            alwaysAcquireMediaFirst: true
        }
    }, {
        // 在这里定义 delegate 对象
        onCallReceived: (session) => {
            console.log("[phone]收到来电......")
            // 获取自动接通按钮的状态
            let autoAnswer = document.getElementById("auto-answer").checked;
            // 如果自动接通被选中，直接接听
            if (autoAnswer) {
                console.log("[phone]自动接听")
                session.answer(mediaOptions)
                    .catch((error) => alert(error));
            }
        },
        onCallHangup: (session) => {
            endCall(session);
        }
    });
    // 连接到服务器并注册
    simpleUser.connect()
        .then(() => getSpan("connect_status").textContent = "已连接")
        .catch((error) => alert(error));
});


getButton("unconnect").addEventListener("click", () => {
    // 注销并断开连接
    simpleUser.disconnect()
        .then(() => getSpan("connect_status").textContent = "未连接")
        .catch((error) => alert(error));
});

// 注册按钮点击事件
getButton("register").addEventListener("click", () => {
    // 连接到服务器并注册
    simpleUser.register({})
        .then(() => getSpan("status").textContent = "已注册")
        .catch((error) => alert(error));
});


// 注销按钮点击事件
getButton("unregister").addEventListener("click", () => {
    // 注销并断开连接
    simpleUser.unregister({}) // 传递一个空对象作为参数
        .then(() => getSpan("status").textContent = "未注册")
        .catch((error) => alert(error));
});

// 拨打按钮点击事件
getButton("call").addEventListener("click", () => {
    // 获取目标号码
    const target = getInput("target").value;
    if (target) {
        // 发起呼叫，只传递目标地址和媒体选项作为参数
        simpleUser.call(target, mediaOptions)
            .catch((error) => alert(error));
    }
});

function endCall(session) {
    console.log("[phone]结束通话");
    switch (session.state) {
        case SIP.SessionState.Initial:
        case SIP.SessionState.Establishing:
            if (simpleUser.session.constructor.name ===
                "Inviter") {
                // An unestablished outgoing session
                session.cancel();
            } else {
                // An unestablished incoming session
                session.reject();
            }
            break;
        case SIP.SessionState.Established:
            // An established session
            session.bye();
            break;
        case SIP.SessionState.Terminating:
        case SIP.SessionState.Terminated:
            // Cannot terminate a session that is already terminated
            break;
    }
}

// 挂断按钮点击事件
getButton("hangup").addEventListener("click", () => {
    endCall(simpleUser.session);
});

getButton("answer").addEventListener("click", () => {
    console.log("[phone]手动接听")
    session.answer(mediaOptions)
        .catch((error) => alert(error));
});
metadata {

    definition (name: "Simulated Switch (通用)", namespace: "stratevision", author: "Jack Sun") {
        capability "Switch"
        capability "Relay Switch"
        capability "Sensor"
        capability "Actuator"

        command "onPhysical"
        command "offPhysical"
    }

    tiles {
        standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
            state "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
            state "on", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00A0DC"
        }
        standardTile("on", "device.switch", decoration: "flat") {
            state "default", label: 'On', action: "onPhysical", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
        }
        standardTile("off", "device.switch", decoration: "flat") {
            state "default", label: 'Off', action: "offPhysical", icon: "st.switches.switch.on", backgroundColor: "#ffffff"
        }
        main "switch"
        details(["switch"])
    }

    preferences {
        input name: "ipAddress", type: "text", title: "目標地址(IP Address)", description: "請輸入想要控制的目標 IP，例如：10.0.0.12", defaultValue: "10.0.0.12", required: true, displayDuringSetup: true
        input name: "endpoint", type: "text", title: "端點編號(Endpoint)", description: "請輸入想要控制的端點編號，例如：a1", required: true, displayDuringSetup: true
        input name: "roomNumber", type: "text", title: "房間編號(Room Number)", description: "請輸入想要控制的房間編號，例如：1621", required: true, displayDuringSetup: true
        input name: "portNumber", type: "number", title: "通訊埠號(Port Number)", description: "請輸入目標伺服器的通訊埠號，預設值：8866", defaultValue: 8866, required: false, displayDuringSetup: true
    }
}

def parse(description) {
}

def on() {
    log.debug "$version on()"
    sendEvent(name: "switch", value: "on")
    sendRequest("on")
}

def off() {
    log.debug "$version off()"
    sendEvent(name: "switch", value: "off")
    sendRequest("off")
}

def onPhysical() {
    log.debug "$version onPhysical()"
    sendEvent(name: "switch", value: "on", type: "physical")
    sendRequest("on")
}

def offPhysical() {
    log.debug "$version offPhysical()"
    sendEvent(name: "switch", value: "off", type: "physical")
    sendRequest("off")
}

private getVersion() {
    "PUBLISHED"
}

private sendRequest(mode) {
    def result = new physicalgraph.device.HubAction(
        method: "GET",
        path: "/test?roomid=$roomNumber&point=$endpoint&action=$mode",
        headers: [
            HOST: "$ipAddress:$portNumber"
        ]
    )
    sendHubCommand(result)
}
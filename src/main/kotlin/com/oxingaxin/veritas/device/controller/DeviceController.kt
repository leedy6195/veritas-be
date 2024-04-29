package com.oxingaxin.veritas.device.controller

import com.oxingaxin.veritas.common.BaseResponse
import com.oxingaxin.veritas.device.domain.dto.*
import com.oxingaxin.veritas.device.service.DeviceService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/devices")
class DeviceController(
        private val deviceService: DeviceService,
) {
    @PostMapping("/kiosks")
    fun createKiosk(
            @RequestBody kioskCreateRequest: KioskCreateRequest
    ): BaseResponse<Void> {
        deviceService.saveKiosk(kioskCreateRequest)

        return BaseResponse.ok()
    }

    @GetMapping("/kiosks")
    fun getKiosks(): BaseResponse<List<KioskResponse>> {
        val kiosks = deviceService.findKiosks()
        return BaseResponse.ok(kiosks)
    }

    @PutMapping("/kiosks/{kioskId}")
    fun updateKiosk(
            @PathVariable kioskId: Long,
            @RequestBody kioskUpdateRequest: KioskUpdateRequest
    ): BaseResponse<Void> {
        deviceService.updateKiosk(kioskId, kioskUpdateRequest)
        return BaseResponse.ok()
    }

    @DeleteMapping("/kiosks/{kioskId}")
    fun deleteKiosk(
            @PathVariable kioskId: Long
    ): BaseResponse<Void> {
        deviceService.deleteKiosk(kioskId)
        return BaseResponse.ok()
    }

    @PostMapping("/entryDevices")
    fun createEntryDevice(
            @RequestBody entryDeviceCreateRequest: EntryDeviceCreateRequest
    ): BaseResponse<Void> {
        deviceService.saveEntryDevice(entryDeviceCreateRequest)
        return BaseResponse.ok()
    }

    @GetMapping("/entryDevices")
    fun getEntryDevices(): BaseResponse<List<EntryDeviceResponse>> {
        val entryDevices = deviceService.findEntryDevices()
        return BaseResponse.ok(entryDevices)
    }

    @GetMapping("/entryDevices/{deviceId}")
    fun getEntryDevice(
            @PathVariable deviceId: Long
    ): BaseResponse<EntryDeviceResponse> {
        val entryDevice = deviceService.findEntryDevice(deviceId)
        return BaseResponse.ok(entryDevice)
    }

    @PutMapping("/entryDevices/{deviceId}")
    fun updateEntryDevice(
            @PathVariable deviceId: Long,
            @RequestBody entryDeviceUpdateRequest: EntryDeviceUpdateRequest
    ): BaseResponse<Void> {
        deviceService.updateEntryDevice(deviceId, entryDeviceUpdateRequest)
        return BaseResponse.ok()
    }

    @DeleteMapping("/entryDevices/{deviceId}")
    fun deleteEntryDevice(
            @PathVariable deviceId: Long
    ): BaseResponse<Void> {
        deviceService.deleteEntryDevice(deviceId)
        return BaseResponse.ok()
    }
}

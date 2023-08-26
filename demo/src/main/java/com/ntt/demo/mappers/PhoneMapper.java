package com.ntt.demo.mappers;

import com.ntt.demo.dto.PhoneDTO;
import com.ntt.demo.entity.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneMapper {
    public static Phone toModel(PhoneDTO phoneDTO) {
        Phone phone = new Phone();
        phone.setId(phoneDTO.getId());
        phone.setNumber(phoneDTO.getNumber() != null ? phoneDTO.getNumber() : null);
        phone.setCityCode(phoneDTO.getCytycode() != null ? phoneDTO.getCytycode() : null);
        phone.setCountryCode(phoneDTO.getCountrycode() != null ? phoneDTO.getCountrycode() : null);
        return phone;
    }

    public static PhoneDTO toDTO(Phone phone) {
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber(phone.getNumber() != null ? phone.getNumber() : null);
        phoneDTO.setCytycode(phone.getCityCode() != null ? phone.getCityCode() : null);
        phoneDTO.setCountrycode(phone.getCountryCode() != null ? phone.getCountryCode() : null);
        return phoneDTO;
    }

    public static List<PhoneDTO> phoneDTOList(List<Phone> phoneList){
        List<PhoneDTO> phoneDTOList = new ArrayList<>();
        if(phoneList != null && !phoneList.isEmpty()){
            for (Phone phone : phoneList){
                PhoneDTO dto = toDTO(phone);
                phoneDTOList.add(dto);
            }
        }
        return phoneDTOList;
    }

    public static List<Phone> phoneList(List<PhoneDTO> phoneDTOList){
        List<Phone> phoneList = new ArrayList<>();
        if(phoneDTOList != null && !phoneDTOList.isEmpty()){
            for (PhoneDTO dto : phoneDTOList){
                Phone model = toModel(dto);
                phoneList.add(model);
            }
        }
        return phoneList;
    }
}

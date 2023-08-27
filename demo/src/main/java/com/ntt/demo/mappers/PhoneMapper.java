package com.ntt.demo.mappers;

import com.ntt.demo.dto.PhoneDTO;
import com.ntt.demo.entity.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneMapper {
    public static Phone toModel(PhoneDTO phoneDTO) {
        Phone phone = new Phone();
        if(phoneDTO != null){
            phone.setId(phoneDTO.getId() != null ? phoneDTO.getId() : null);
            phone.setNumber(phoneDTO.getNumber() != null ? phoneDTO.getNumber() : null);
            phone.setCityCode(phoneDTO.getCitycode() != null ? phoneDTO.getCitycode() : null);
            phone.setContryCode(phoneDTO.getContrycode() != null ? phoneDTO.getContrycode() : null);
        }

        return phone;
    }

    public static PhoneDTO toDTO(Phone phone) {
        PhoneDTO phoneDTO = new PhoneDTO();
        if(phone != null){
            phoneDTO.setId(phone.getId() != null ? phone.getId() : null);
            phoneDTO.setNumber(phone.getNumber() != null ? phone.getNumber() : null);
            phoneDTO.setCitycode(phone.getCityCode() != null ? phone.getCityCode() : null);
            phoneDTO.setContrycode(phone.getContryCode() != null ? phone.getContryCode() : null);
        }

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

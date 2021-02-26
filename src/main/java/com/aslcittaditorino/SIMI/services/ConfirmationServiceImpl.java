/*package com.aslcittaditorino.SIMI.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

     @Autowired
    ConfirmationRepository confirmationRepository;
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    SimRepository simRepository;

    @Autowired
    GrantRepository grantRepository;
    @Autowired
    ParameterRepository parameterRepository;
    @Autowired
    ModelMapper modelMapper;


    @Override
    public Long getDaysExpiringConfirmations(){
        Parameter temp = new Parameter();
        if(!parameterRepository.findById("days").isPresent()){
            throw new ConfirmationServiceException("Non è stato mai impostato timer notifiche");
        }
        parameterRepository.findById("days").ifPresent(item->{
            temp.setName(item.getName());
            temp.setValue(item.getValue());
        });
        return temp.getValue();
    }

    @Override
    public void setDaysExpiringConfirmations(Long days) {
        Parameter temp = new Parameter();
        temp.setValue(days); temp.setName("days");
        parameterRepository.save(temp);
    }

    @Override
    public int getNumberExpiringConfirmations() {
        Date date = new Date();
        Parameter temp = new Parameter();
        if(!parameterRepository.findById("days").isPresent()){
            throw new ConfirmationServiceException("Non è stato mai impostato timer notifiche");
        }
        parameterRepository.findById("days").ifPresent(item->{
            temp.setName(item.getName());
            temp.setValue(item.getValue());
        });

        return confirmationRepository.countByNextConfirmationBeforeAndNextConfirmationAfterAndConfirmedIsNot(new Timestamp(date.getTime()+Math.round(8.64e+7*temp.getValue())), new Timestamp(date.getTime()),true);
    }


    @Override
    public List<ConfirmationDTO> getExpiringConfirmations(Long days) {
        List<ConfirmationDTO> tempList = new ArrayList<>();
        Parameter tempDays = new Parameter();
        parameterRepository.findById("days").ifPresent(item ->{
            tempDays.setName("days");
            tempDays.setValue(item.getValue());
        });
        Date date = new Date();
        confirmationRepository.findByNextConfirmationBeforeAndNextConfirmationAfter(new Timestamp(date.getTime()+Math.round(8.64e+7*tempDays.getValue())), new Timestamp(date.getTime()))
                .ifPresent(list->{
                    list.stream().forEach(item->{
                        if(!item.isConfirmed())
                        {
                            ConfirmationDTO temp= new ConfirmationDTO();
                            temp = modelMapper.map(item,ConfirmationDTO.class);
                            tempList.add(temp);
                        }
                    });
                });
        return tempList;
    }

    @Override
    public List<ConfirmationDTO> getConfirmations(boolean active) {
        List<ConfirmationDTO> tempList = new ArrayList<>();
        confirmationRepository.findByConfirmedEquals(active).ifPresent(list->{

            list.stream().forEach(item->{
                    ConfirmationDTO temp= new ConfirmationDTO();
                    temp = modelMapper.map(item,ConfirmationDTO.class);
                    tempList.add(temp);

            });
        });
        return tempList;
    }

    @Override
    public void confirm(Long id, Optional<ConfirmationDTO> body) {
        Confirmation temp = new Confirmation();
        if(!confirmationRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"L'id specificato non esiste");
        }
        temp = confirmationRepository.findById(id).get();
        temp.setConfirmed(true);
        confirmationRepository.save(temp);

       body.ifPresent(item->{
            if(item.getLastConfirmation() == null)
                return;
            System.out.println("item è " + item);
            Confirmation newTemp = new Confirmation();
            Sim tempSim = new Sim();
            Device tempDevice = new Device();
            if(Optional.ofNullable(item.getDeviceImei()).isPresent()){
                System.out.println("sono in device");
                tempDevice =deviceRepository.findById(item.getDeviceImei()).get();
                newTemp.setDevice(tempDevice);
            }
            if(Optional.ofNullable(item.getSimIccid()).isPresent()){
                System.out.println("sono in sim");
                tempSim =simRepository.findById(item.getSimIccid()).get();
                newTemp.setSim(tempSim);
            }
            System.out.println("finito sim e device");
            newTemp.setConfirmed(false);
            newTemp.setId(Long.valueOf(0));
            newTemp.setNextConfirmation(item.getNextConfirmation());
            newTemp.setLastConfirmation(item.getLastConfirmation());
            System.out.println("mi buggo qui");
            Grant tempGrant = new Grant();
            tempGrant.setId(item.getGrantId());
            newTemp.setGrant(tempGrant);
            System.out.println("sto per inserire" + newTemp);
            confirmationRepository.save(newTemp);
        });

    }
}
*/
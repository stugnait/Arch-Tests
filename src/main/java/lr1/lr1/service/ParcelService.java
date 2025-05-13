package lr1.lr1.service;

import lr1.lr1.model.Parcel;
import lr1.lr1.repository.ParcelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParcelService {

    private static ParcelRepository parcelRepository;

    @Autowired
    public ParcelService(ParcelRepository parcelRepository) {
        ParcelService.parcelRepository = parcelRepository;
    }

    public Parcel getParcelById(Long id) {
        return parcelRepository.findById(id).orElseThrow();
    }

    public static Parcel createParcel(Parcel parcel) {
        return parcelRepository.save(parcel);
    }

    public Parcel updateParcel(Long id, Parcel parcel) {

        parcel.setId(id);
        return parcelRepository.save(parcel);
    }

    public void deleteParcel(Long id) {
        parcelRepository.deleteById(id);
    }
}

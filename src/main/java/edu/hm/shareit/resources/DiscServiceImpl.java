package edu.hm.shareit.resources;

import edu.hm.fachklassen.Disc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Created by oliver on 24.04.17.
 */
public class DiscServiceImpl implements DiscService {
    final static Set<Disc> discs = new HashSet<>();

    @Override
    public DiscServiceResult addDisc(Disc disc) {
        final DiscServiceResult discServiceResult;

        if (disc.getBarcode().isEmpty()) {
            discServiceResult = DiscServiceResult.MissingParamBarcode;
        } else if (disc.getDirector().isEmpty()) {
            discServiceResult = DiscServiceResult.MissingParamDirector;
        } else if (disc.getTitle().isEmpty()) {
            discServiceResult = DiscServiceResult.MissingParamTitle;
        } else if (disc.getFsk() < 0) {
            discServiceResult = DiscServiceResult.NegativeFSK;
        } else if (getDisc(disc.getBarcode()) != null) {
            discServiceResult = DiscServiceResult.BarcodeAlreadyExists;
        } else if (disc.getBarcode().length() != 13) {
            discServiceResult = DiscServiceResult.BarcodeInvalidLength;
        } else {
            discServiceResult = DiscServiceResult.AllRight;
            discs.add(disc);
        }

        return discServiceResult;
    }

    @Override
    public Disc getDisc(String barcode) {
        Optional<Disc> discList = discs.stream()
                .filter(disc -> disc.getBarcode().equals(barcode))
                .findFirst();

        return discList.isPresent() ? discList.get() : null;
    }

    @Override
    public Disc[] getDiscs() {
        return discs.toArray(new Disc[]{});
    }

    @Override
    public DiscServiceResult updateDisc(Disc disc) {
        final DiscServiceResult discServiceResult;
        Optional<Disc> toDelete = discs.stream().filter(other -> other.getBarcode().equals(disc.getBarcode())).findFirst();

        if (toDelete.isPresent()) {
            discs.remove(toDelete.get());
            discs.add(disc);
            discServiceResult = DiscServiceResult.AllRight;
        } else {
            discServiceResult = DiscServiceResult.NoDiscWithBarcodeFound;
        }

        return discServiceResult;
    }
}

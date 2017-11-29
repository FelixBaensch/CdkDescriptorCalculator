package model;

//<editor-fold defaultstate="collapsed" desc="imports">
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.IAtomPairDescriptor;
import org.openscience.cdk.qsar.IAtomicDescriptor;
import org.openscience.cdk.qsar.IBondDescriptor;
import org.openscience.cdk.qsar.IDescriptor;
import org.openscience.cdk.qsar.IMolecularDescriptor;
import org.openscience.cdk.qsar.result.BooleanResult;
import org.openscience.cdk.qsar.result.BooleanResultType;
import org.openscience.cdk.qsar.result.DoubleArrayResultType;
import org.openscience.cdk.qsar.result.DoubleResult;
import org.openscience.cdk.qsar.result.DoubleResultType;
import org.openscience.cdk.qsar.result.IDescriptorResult;
import org.openscience.cdk.qsar.result.IntegerArrayResultType;
import org.openscience.cdk.qsar.result.IntegerResult;
import org.openscience.cdk.qsar.result.IntegerResultType;
import util.UtilityMethods;
//</editor-fold>

/**
 * Calculator class for the descriptor calculation
 *
 * @author Felix Bänsch, 06.06.2016
 */
public class Calculator {

    //<editor-fold defaultstate="collapsed" desc="private variables">
    private Data data;                  //Instance for the molecule data
    private IDescriptor descriptor;     //Instance for the IDescriptor
    private IAtomContainer container;   //Instance for the IAtomContainer
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructor">
    /**
     * Creates a new Calculator instance
     * @param aData Data
     */
    public Calculator(Data aData) {
        //<editor-fold defaultstate="collapsed" desc="check">
        if (aData == null) {
            throw new IllegalArgumentException("aData must not be null");
        }
        //</editor-fold>
        this.data = aData;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="public methods">
    //<editor-fold defaultstate="collapsed" desc="calculateDescriptors">
    /**
     * Calculates a list of descriptors and put it to the resultMap
     *
     * @param aDescriptorList List
     */
    public void calculateDescriptors(List<IDescriptor> aDescriptorList) {
        
        System.out.println("Calculation started");
        //<editor-fold defaultstate="collapsed" desc="check">
        if (aDescriptorList == null) {
            throw new IllegalArgumentException("aDescriptorList must not be null");
        }
        if (aDescriptorList.isEmpty()){
            throw new IllegalArgumentException("aDescriptorList must not be empty");
        }
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="old">
//        int tmpLoop = this.data.getMoleculeCount();
//        Molecule tmpMolecule;
//        IDescriptorResult tmpResult = null;
//        for (int i = 1; i <= tmpLoop; i++) {
//            if (this.data.hasKey(i)) {
//                Molecule tmpMolecule = this.data.getMolecule(i);
//                IDescriptorResult tmpResult = null;
//                for (IDescriptor tmpDescriptor : aDescriptorList) {
//                    try {
//                        tmpResult = Calculator.this.calculate(tmpDescriptor, tmpMolecule);
//                        tmpMolecule.addResult(tmpDescriptor, tmpResult);
//                    } catch (Exception ex) {
//                        tmpMolecule.addResult(tmpDescriptor, null);
//                        System.err.println(tmpMolecule.getIdentifier().toString("Counter") + " " + tmpMolecule.getName() + " " + ex.toString() + " " + UtilityMethods.getDescriptorName(tmpDescriptor));
//                    }
//                }
//            } else {
//                tmpLoop++;
//            }
//        }
//        for(IDescriptor tmpDescriptor : aDescriptorList){
//            for(int i = 1; i <= tmpLoop; i++){
//                if(this.data.hasKey(i)){
//                    tmpMolecule = this.data.getMolecule(i);
//                    try{
//                        tmpResult = Calculator.this.calculate(tmpDescriptor, tmpMolecule);
//                        tmpMolecule.getResultsMap().put(tmpDescriptor, tmpResult);
//                    } catch (Exception ex){
//                        tmpMolecule.getResultsMap().put(tmpDescriptor, null);
//                        System.err.println(tmpMolecule.getIdentifier().toString("Counter") + " " + tmpMolecule.getName() + " " + ex.toString() + " " + UtilityMethods.getDescriptorName(tmpDescriptor));
//                    }
//                }
//            }
//        }
        //</editor-fold>

        Set<Integer> tmpKeySet = this.data.getMoleculesMap().keySet();
        if (this.data.getMoleculeCount() < 10) {
            aDescriptorList.forEach(tmpDescriptor -> {
                tmpKeySet.forEach(tmpKey -> {
                    Molecule tmpMolecule = this.data.getMolecule(tmpKey);
                    try {
                        this.calculate(tmpDescriptor, tmpMolecule);
                    } catch (Exception ex) {
                        tmpMolecule.getResultsMap().put(tmpDescriptor, this.getErrorResult(new ExceptionResultType()));
                        System.err.println(tmpMolecule.getIdentifier().toString("Counter") + " " + tmpMolecule.getName() + " " + ex.toString() + " " + UtilityMethods.getDescriptorName(tmpDescriptor));
                    }
                });
            });
        } else {
            aDescriptorList.parallelStream().forEachOrdered(tmpDescriptor -> {
                tmpKeySet.forEach(tmpKey -> {
                    Molecule tmpMolecule = this.data.getMolecule(tmpKey);
                    try {
                        this.calculate(tmpDescriptor, tmpMolecule);                      
                    } catch (Exception ex) {
                        tmpMolecule.getResultsMap().put(tmpDescriptor, this.getErrorResult(new ExceptionResultType()));
                        System.err.println(tmpMolecule.getIdentifier().toString("Counter") + " " + tmpMolecule.getName() + " " + ex.toString() + " " + UtilityMethods.getDescriptorName(tmpDescriptor));
                    }
                });
            });                       
        }
        System.out.println("Calculation finished");
    }
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="private methods">
    //<editor-fold defaultstate="collapsed" desc="oldCalculate">
    /**
     * Calculates the given IDescriptor for the givne IAtomContainer
     *
     * @param aDescriptor IDescriptor
     * @param aMolecule Molecule for which the IDescriptor should be calculated
     * @return DescriptorValue IDescriptorResult of the calculation
     */
    private IDescriptorResult oldCalculate(IDescriptor aDescriptor, Molecule aMolecule) {
        this.descriptor = aDescriptor;
        this.container = aMolecule.getIAtomContainer();
        DescriptorValue tmpValue = null;
        if (this.descriptor instanceof IAtomicDescriptor) {
            for (IAtom tmpAtom : this.container.atoms()) {
                try {
                    tmpValue = ((IAtomicDescriptor) this.descriptor).calculate(tmpAtom, this.container);
                    if (tmpValue.getException() == null) {
                        return tmpValue.getValue();
                    } else {
                        System.err.println(aMolecule.getName() + aMolecule.getIdentifier().toString("Counter") + tmpValue.getException().toString() + UtilityMethods.getDescriptorName(this.descriptor));
                        return this.getErrorResult(tmpValue.getValue());
                    }
                } catch (Exception ex) {
                    System.err.println(aMolecule.getName() + " " + aMolecule.getIdentifier().toString("Counter") + " " + ex.toString() + " " + UtilityMethods.getDescriptorName(this.descriptor));
                }
            }
        } else if (this.descriptor instanceof IAtomPairDescriptor) {
            for (IAtom tmpAtom1 : this.container.atoms()) {
                for (IAtom tmpAtom2 : this.container.atoms()) {
                    if (!tmpAtom1.equals(tmpAtom2)) {
                        tmpValue = ((IAtomPairDescriptor) this.descriptor).calculate(tmpAtom1, tmpAtom2, this.container);
                        if (tmpValue.getException() == null) {
                            return tmpValue.getValue();
                        } else {
                            System.err.println(aMolecule.getName() + aMolecule.getIdentifier().toString("Counter") + tmpValue.getException().toString() + UtilityMethods.getDescriptorName(this.descriptor));
                            return this.getErrorResult(tmpValue.getValue());
                        }
                    }
                }
            }
        } else if (this.descriptor instanceof IBondDescriptor) {
            for (IBond tmpBond : this.container.bonds()) {
                tmpValue = ((IBondDescriptor) this.descriptor).calculate(tmpBond, this.container);
                if (tmpValue.getException() == null) {
                    return tmpValue.getValue();
                } else {
                    System.err.println(aMolecule.getName() + aMolecule.getIdentifier().toString("Counter") + tmpValue.getException().toString() + UtilityMethods.getDescriptorName(this.descriptor));
                    return this.getErrorResult(tmpValue.getValue());
                }
            }
        } else if (this.descriptor instanceof IMolecularDescriptor) {
            tmpValue = ((IMolecularDescriptor) this.descriptor).calculate(this.container);
            if (tmpValue.getException() == null) {
                return tmpValue.getValue();
            } else {
                System.err.println(aMolecule.getName() + aMolecule.getIdentifier().toString("Counter") + tmpValue.getException().toString() + UtilityMethods.getDescriptorName(this.descriptor));
                return this.getErrorResult(tmpValue.getValue());
            }
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="calculate">
    /**
     * Calculates the given IDescriptor for the given Molecule
     *
     * @param aDescriptor IDescriptor which should be calculated
     * @param aMolecule Molecule for which the descriptor should be calculated
     */
    private void calculate(IDescriptor aDescriptor, Molecule aMolecule) {
        this.descriptor = aDescriptor;
        this.container = aMolecule.getIAtomContainer();
        DescriptorValue tmpValue = null;
        if (this.descriptor instanceof IAtomicDescriptor) {
            for (IAtom tmpAtom : this.container.atoms()) {
                try {
                    tmpValue = ((IAtomicDescriptor) this.descriptor).calculate(tmpAtom, this.container);
                    if (tmpValue.getException() == null) {                        
                        aMolecule.getResultsMap().put(this.descriptor, tmpValue.getValue());                        
                    } else {
                        System.err.println(aMolecule.getName() + aMolecule.getIdentifier().toString("Counter") + tmpValue.getException().toString() + UtilityMethods.getDescriptorName(this.descriptor));
                        aMolecule.getResultsMap().put(this.descriptor, this.getErrorResult(tmpValue.getValue()));
                    }
                } catch (Exception ex) {
                    System.err.println(aMolecule.getName() + " " + aMolecule.getIdentifier().toString("Counter") + " " + ex.toString() + " " + UtilityMethods.getDescriptorName(this.descriptor));
                    aMolecule.getResultsMap().put(this.descriptor, this.getErrorResult(new ExceptionResult()));
                }
            }
        } else if (this.descriptor instanceof IAtomPairDescriptor) {
            for (IAtom tmpAtom1 : this.container.atoms()) {
                for (IAtom tmpAtom2 : this.container.atoms()) {
                    if (!tmpAtom1.equals(tmpAtom2)) {
                        try {
                            tmpValue = ((IAtomPairDescriptor) this.descriptor).calculate(tmpAtom1, tmpAtom2, this.container);
                            if (tmpValue.getException() == null) {
                                aMolecule.getResultsMap().put(this.descriptor, tmpValue.getValue());
                            } else {
                                System.err.println(aMolecule.getName() + aMolecule.getIdentifier().toString("Counter") + tmpValue.getException().toString() + UtilityMethods.getDescriptorName(this.descriptor));
                                aMolecule.getResultsMap().put(this.descriptor, this.getErrorResult(tmpValue.getValue()));
                            }
                        } catch (Exception ex) {
                            System.err.println(aMolecule.getName() + " " + aMolecule.getIdentifier().toString("Counter") + " " + ex.toString() + " " + UtilityMethods.getDescriptorName(this.descriptor));
                            aMolecule.getResultsMap().put(this.descriptor, this.getErrorResult(new ExceptionResult()));
                        }
                    }
                }
            }
        } else if (this.descriptor instanceof IBondDescriptor) {
            for (IBond tmpBond : this.container.bonds()) {
                try {
                    tmpValue = ((IBondDescriptor) this.descriptor).calculate(tmpBond, this.container);
                    if (tmpValue.getException() == null) {
                        aMolecule.getResultsMap().put(this.descriptor, tmpValue.getValue());
                    } else {
                        System.err.println(aMolecule.getName() + aMolecule.getIdentifier().toString("Counter") + tmpValue.getException().toString() + UtilityMethods.getDescriptorName(this.descriptor));
                        aMolecule.getResultsMap().put(this.descriptor, this.getErrorResult(tmpValue.getValue()));
                    }
                } catch (Exception ex) {
                    System.err.println(aMolecule.getName() + " " + aMolecule.getIdentifier().toString("Counter") + " " + ex.toString() + " " + UtilityMethods.getDescriptorName(this.descriptor));
                    aMolecule.getResultsMap().put(this.descriptor, this.getErrorResult(new ExceptionResult()));
                }
            }
        } else if (this.descriptor instanceof IMolecularDescriptor) {
            try {
                tmpValue = ((IMolecularDescriptor) this.descriptor).calculate(this.container);
                if (tmpValue.getException() == null) {
                    aMolecule.getResultsMap().put(this.descriptor, tmpValue.getValue());
                } else {
                    System.err.println(aMolecule.getName() + aMolecule.getIdentifier().toString("Counter") + tmpValue.getException().toString() + UtilityMethods.getDescriptorName(this.descriptor));
                    aMolecule.getResultsMap().put(this.descriptor, this.getErrorResult(tmpValue.getValue()));
                }
            } catch (Exception ex) {
                System.err.println(aMolecule.getName() + " " + aMolecule.getIdentifier().toString("Counter") + " " + ex.toString() + " " + UtilityMethods.getDescriptorName(this.descriptor));
                aMolecule.getResultsMap().put(this.descriptor, this.getErrorResult(new ExceptionResult()));
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getErrorResult">
    /**
     * Gets the ErrorResult according to the ResultType
     *
     * @param aResult IDescriptorResult
     * @return IErrorResult
     */
    public IErrorResult getErrorResult(IDescriptorResult aResult) {
        if (aResult instanceof BooleanResult || aResult instanceof BooleanResultType) {
            return new BooleanErrorResult();
        } else if (aResult instanceof DoubleResult || aResult instanceof DoubleResultType || aResult instanceof DoubleArrayResultType) {
            return new DoubleErrorResult();
        } else if (aResult instanceof IntegerResult || aResult instanceof IntegerResultType || aResult instanceof IntegerArrayResultType) {
            return new IntegerErrorResult();
        } else if (aResult instanceof ExceptionResultType){
            return new ExceptionResult();
        } else {
            return null;
        }
    }
    //</editor-fold>
    //</editor-fold>
}

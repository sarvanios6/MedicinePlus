package com.sarvan.medicineplus.others;

/**
 * Created by Sarvan on 25/01/17.
 */

public enum ConstDef {

    //https://www.jmu.edu/esol/specialist_list.htm
    GENERAL("Genral"),
    GENERAL_DESC("Treating patients suffering from all types of medical condition."),
    ALLERGIST("Allergist or Immunologist"),
    ALLERGISTDESC("Conducts the diagnosis and treatment of allergic conditions."),
    ANESTHESIOLIST("Anesthesiologist"),
    ANESTHESIOLISTDESC("Treats chronic pain syndromes, administers anesthesia and monitors the patient during surgery."),
    CARDIOLOGIST("Cardiologist"),
    CARDIOLOGISTDESC("Treats heart disease"),
    DERMATOLOGIST("Dermatologist"),
    DERMATOLOGISTDESC("Treats skin diseases, including some skin cancers"),
    GASTROENTEROLOGIST("Gastroenterologist"),
    GASTROENTEROLOGISTDESC("Treats stomach disorders"),
    HEMATOLOGIST("Hematologist"),
    NEPHROLOGIST("Nephrologist"),
    NEUROLOGIST("Neurologist"),
    NEUROSURGEON("Neurosurgeon"),
    OBSTETRICIAN("Obstetrician"),
    GYNECOLOGIST("Gynecologist"),
    OPHTHALMOLOGIST("Ophthalmologist"),
    PTOLARYNGOLOGIST("Otolaryngologist"),
    PATHOLOGIST("Pathologist"),
    PEDIATRICIAN("Pediatrician"),
    PODIATRIST("Podiatrist"),
    PSYCHIATRIST("Psychiatrist"),
    UROLOGIST("Urologist");

    // Key
    private String key;
    // Value
    private int value;

    // String Constructor
    ConstDef(String key) {
        this.key = key;
    }

    // Integer Constructor
    ConstDef(int value) {
        this.value = value;
    }

    // Get key
    public String getKey() {
        return key;
    }

    // Get value
    public int getValue() {
        return value;
    }
}

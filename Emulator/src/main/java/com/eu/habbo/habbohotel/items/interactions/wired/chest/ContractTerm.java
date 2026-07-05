package com.eu.habbo.habbohotel.items.interactions.wired.chest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A single PAY/RECEIVE currency term from a wired contract extra.
 * Wire encoding: {@code intParams = [count, dir, type, amount, ...]} where dir 0=PAY, 1=RECEIVE.
 */
public class ContractTerm {
    public static final int DIR_PAY = 0;
    public static final int DIR_RECEIVE = 1;

    public final int direction;
    public final int currencyType;
    public final int amount;

    public ContractTerm(int direction, int currencyType, int amount) {
        this.direction = direction;
        this.currencyType = currencyType;
        this.amount = Math.max(0, amount);
    }

    public static List<ContractTerm> parse(int[] params) {
        if (params == null || params.length < 1) {
            return Collections.emptyList();
        }

        int count = Math.max(0, params[0]);
        List<ContractTerm> terms = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            int base = 1 + i * 3;
            if (base + 2 >= params.length) {
                break;
            }
            int amount = Math.max(0, params[base + 2]);
            if (amount <= 0) {
                continue;
            }
            terms.add(new ContractTerm(params[base], params[base + 1], amount));
        }

        return terms;
    }

    public static int[] serialize(List<ContractTerm> terms) {
        List<ContractTerm> valid = new ArrayList<>();
        for (ContractTerm term : terms) {
            if (term != null && term.amount > 0) {
                valid.add(term);
            }
        }

        int[] params = new int[1 + valid.size() * 3];
        params[0] = valid.size();
        for (int i = 0; i < valid.size(); i++) {
            ContractTerm term = valid.get(i);
            int base = 1 + i * 3;
            params[base] = term.direction;
            params[base + 1] = term.currencyType;
            params[base + 2] = term.amount;
        }
        return params;
    }
}

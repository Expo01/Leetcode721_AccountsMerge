import java.util.*;



class Solution { // 3 steps, build adjacency list, merge accounts for a user, sort merged list and add to return list

    HashSet<String> visited = new HashSet<>();
    Map<String, List<String>> adjacent = new HashMap<String, List<String>>();

    public List<List<String>> accountsMerge(List<List<String>> accountList) {
        int accountListSize = accountList.size(); // # of accounts

        for (List<String> account : accountList) {
            int accountSize = account.size(); // # of items in account

            // Building adjacency list. Adding edge between first email to all other emails in the account
            String accountFirstEmail = account.get(1);
            for (int j = 2; j < accountSize; j++) { // loop all emails after start email
                String accountEmail = account.get(j);

                if (!adjacent.containsKey(accountFirstEmail)) {
                    adjacent.put(accountFirstEmail, new ArrayList<String>());
                }
                adjacent.get(accountFirstEmail).add(accountEmail); // add current email to start email List value

                if (!adjacent.containsKey(accountEmail)) {
                    adjacent.put(accountEmail, new ArrayList<String>());
                }
                adjacent.get(accountEmail).add(accountFirstEmail); // add start email to current email list val
                // suppose 5 emails
                /*
                             2     3       {1,(2,3,4,5)} email one contains edges with all
                              \   /        {2,(1)}
                                1          {3,(1)} other emails only 1 edge with axis node for leastt # of edges to connect all emails
                              /   \        {etc.}
                             4     5
                 */

            }
        } // at this point this cog and spoke pattern created for all accounts. account holder name not handled


        List<List<String>> mergedAccounts = new ArrayList<>(); // final return List
        for (List<String> account : accountList) {  // Traverse over all the accounts again
            String accountName = account.get(0);
            String accountFirstEmail = account.get(1); // now using 'cog' that was generated


            if (!visited.contains(accountFirstEmail)) { // DFS if 'cog' not visited
                List<String> mergedAccount = new ArrayList<>(); // contains merged accounts for single user from all 'accounts'
                mergedAccount.add(accountName); // Adding account name at the 0th index

                DFS(mergedAccount, accountFirstEmail); // function performed
                Collections.sort(mergedAccount.subList(1, mergedAccount.size())); // sort emails, not the first name
                mergedAccounts.add(mergedAccount); // add single user merged accounts to return List
            }
        }

        return mergedAccounts;
    }


    private void DFS(List<String> mergedAccount, String email) {
        visited.add(email); // add current email to visited
        mergedAccount.add(email); // add email to complete merged list for user

        if (!adjacent.containsKey(email)) { // i'm not sure how this would happen? all emails should be a key to store edges/neighbors
            return; // without this though gets null pointer exception since email is null? how?
        }

        for (String neighbor : adjacent.get(email)) { // will explore all spokes, understanding that one of the emails will
            if (!visited.contains(neighbor)) { // be a spoke for multiple cogs
                DFS(mergedAccount, neighbor); // and by searching that common spoke, will DFS the whole neighboring 'wheel'
                // related to above return statement, how would a null ever be passed from adjacency list?
            }
        }
    }

                    /*
                            2     3     8     9
                              \   /     \   /
                                1         6
                              /   \     /   \       see how 5 is the common spoke connecting two wheels
                             4       5       7      and gives access to cog of neighboring wheel for further DFS
                 */

}




/////////////////

// ATTEMPT

//
//class Solution {
//    public List<List<String>> accountsMerge(List<List<String>> accounts) {
//        Map<String, ArrayList<String>> links = new HashMap<>();
//        List<List<String>> ans = new ArrayList<>();
//
//        for (List<String> name : accounts) { // loops through all lists of (name, email, email)
//            for (int i = 1; i < name.size(); i++) { // starts at first email
//                if (!links.containsKey(i)) {
//                    links.put(name.get(i), new ArrayList<>(name)); // puts email as a key with value of the list (name, email, email)
//                    List<String> temp = links.get(name.get(i)); // acquires the arrayList value for key: email
//                    temp.remove(i); // removes itself from its own arraylist value
//                    // net result is all emails will be mapped to the name and all non-self emails in a lis
//                } else {
//                    Set<String> convert = new HashSet<>(name); // adds current list, say (john, e1,e2,e3)
//                    convert.addAll(links.get(i)); // adds mapped list, say key e3 adds (john, e1,e2)
//                    // at this point this is non-alphabetized and only contains contents from redundant email lists,
//                    // not lists that all user emails already merged
//
//                }
//
//            }
//        }
//
//    }
//}

// basically, given a nested list where each list contains a name at index 0 and all other indexes are emails that
// belong to that person, want to merge all email accounts where an account holder is ID'ed by both name and having
// redundant email in multiple of the nested lists. Order emails alphabetically after merger
// if no email occurs 2+ times among the inner lists, just sort emails for that person alphabetically in the nested list

// graph we have 2 options: DFS, BFS. since a person can have many emails, sounds like course schedule and clone graph
// where courses mapped with any number of pre-reqs and clone graph goes through any number of neighbors
// one step harder than course schedule though since need two factors: same name at index 0 and a redundant email where
// course numbers cannot be same named.

// map emails, if email found, then merge the accounts. but can't just have 'john' as a value. would need to have a key
// for each eail with arraylist avalue of "name, email2, email3" since you never know which email will be found as duplicate
// could then have a set that doesn't allow for redundancies so each time a redundant email found, it tries to re-input
// which is a big waste of time, but idk other ideas. eventually whole 'accounts' List will be iterated through.
// the first name will automatically be at front of the set since any extranous numbers/letters etc. makes it futher to R
// alphabetically. wouold then need to turn set into list and add to outer list
(ns project-factual.data.db
  (:require [cljs.spec :as s]))

;; DATABASE SPEC

(s/def ::item.id int?)
(s/def ::item.type keyword?)
(s/def ::item.content string?)
(s/def ::item.item (s/keys :req-un [::item.id ::item.type ::item.content]))
(s/def ::items (s/map-of ::item.id ::item.item))

(s/def ::group.id int?)
(s/def ::group.type keyword?)
(s/def ::group.name string?)
(s/def ::group.group (s/keys :req-un [::group.id ::group.type ::group.name]))
(s/def ::groups (s/map-of ::group.id ::group.group))

(s/def ::active-item-id int?)
(s/def ::active-group-id int?)
(s/def ::sidebar-active boolean?)

(s/def ::db (s/keys :req-un [::items ::groups ::active-item-id ::active-group-id ::editor]))

;; ----------
;; MOCK DATA
;; ----------

(defn multiline-string [& strings]
  (clojure.string/join "\n" strings))

(def testing-database
  {:items {1 {:item.id 1
              :item.type :item.markdown
              :item.content "First line\nSecond line"}
           2 {:item.id 2
              :item.type :item.markdown
              :item.content (multiline-string "# Mary had a little lamb"
                                              " "
                                              "Mary had a little lamb  "
                                              "Little lamb  "
                                              "Little lamb  "
                                              " "
                                              "Mary had a little lamb  "
                                              "That was as white as snow")}
           3 {:item.id 3
              :item.type :item.markdown
              :item.content (str "# Intro\nGo ahead, play around with the editor!"
                                  "Be sure to check out **bold** and *italic* styling,"
                                  "or even [links](http://google.com)."
                                  "You can type the Markdown syntax, use the toolbar,"
                                  "or use shortcuts like `cmd-b` or `ctrl-b`.\n\n"
                                  "## Lists\nUnordered lists can be started using"
                                  "the toolbar or by typing `* `, `- `, or `+ `."
                                  "Ordered lists can be started by typing `1. `.\n\n"
                                  "#### Unordered\n"
                                  "* Lists are a piece of cake\n"
                                  "* They even auto continue as you type\n"
                                  "* A double enter will end them\n"
                                  "* Tabs and shift-tabs work too\n\n"
                                  "#### Ordered\n"
                                  "1. Numbered lists...\n"
                                  "2. ...work too!\n"
                                  "3. \n\n## What about images?"
                                  "\n![Yes](http://i.imgur.com/sZlktY7.png)")}}
   :groups {1 {:group.id 1
               :group.name "All"
               :group.type :group.filter
               :group.filter (constantly true)}
            2 {:group.id 2
               :group.name "Default"
               :group.type :group.col
               :group.elements [1 2 3]}}
   :active-item-id 1
   :active-group-id 1
   :sidebar-active false
   :editor nil})
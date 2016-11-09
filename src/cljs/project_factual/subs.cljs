(ns project-factual.subs
  (:require [re-frame.core :as r]
            [clojure.string :as s]
            [clojure.set :as set]))

;;; -----
;;; ITEMS
;;; -----

(r/reg-sub
  :active-item-id
  (fn [db _]
    (:active-item-id db)))

(r/reg-sub
  :items-map
  (fn [db _]
    (:items db)))

(r/reg-sub
  :all-items
  :<- [:items-map]
  (fn [items-map _]
    (vals items-map)))

(r/reg-sub
  :active-item
  :<- [:items-map]
  :<- [:active-item-id]
  (fn [[items-map active-item-id] _]
    (items-map active-item-id)))

;;; ------
;;; GROUPS
;;; ------

(r/reg-sub
  :groups-map
  (fn [db _]
    (:groups db)))

(r/reg-sub
  :active-group-id
  (fn [db _]
    (:active-group-id db)))

(r/reg-sub
  :all-groups
  :<- [:groups-map]
  (fn [groups-map _]
    (vals groups-map)))

(r/reg-sub
  :active-group
  (fn [db _]
    ((:groups db) (:active-group-id db))))

(r/reg-sub
  :all-normal-groups
  :<- [:all-groups]
  (fn [all-groups _]
    (filter #(= :group.collection
                (:group.type %))
            all-groups)))

(r/reg-sub
  :group-all
  (fn [all-groups _]
    project-factual.data.db/group-all))

;;; --------
;;; Groupbar
;;; --------

(r/reg-sub
  :groupbar-suggestions-search
  (fn [db _]
    (:groupbar-suggestions-search db)))

(r/reg-sub
  :groupbar-suggestions-active
  (fn [db _]
    (:groupbar-suggestions-active db)))

;;; ---------------------
;;; Active items & groups
;;; ---------------------

(r/reg-sub
  :active-items
  :<- [:active-group]
  :<- [:all-items]
  :<- [:active-group-id]
  (fn [[active-group all-items active-group-id] _]
    (let [group-type (:group.type active-group)]
      ; If there is ever a need for more types of groups, this needs to be refactored into its own
      ; group handling system with more documentation
      (case group-type
        :group.filter (filter (:group.filter active-group) all-items)
        :group.collection (filter #(contains? (:item.groups %) active-group-id) all-items)))))

(r/reg-sub
  :active-groups
  :<- [:active-item]
  :<- [:groups-map]
  (fn [[active-item groups-map] _]
    (vals (select-keys groups-map
                       (:item.groups active-item)))))

(r/reg-sub
  :groupbar-suggestions
  :<- [:groupbar-suggestions-search]
  :<- [:all-normal-groups]
  :<- [:active-groups]
  (fn [[input groups active-groups] _]
    (sort-by #(:group.name %)
             (set/difference
               (set (filter #(s/includes? (:group.name %) input)
                             groups))
               (set active-groups)))))

;;; ------
;;; OTHERS
;;; ------

(r/reg-sub
  :sidebar-active
  (fn [db _]
    (:sidebar-active db)))

(r/reg-sub
  :screen-dim
  (fn [db _]
    ; The only source of screen dim (currently) is the sidebar
    (:sidebar-active db)))
(ns project-factual.views.main
  "Main view of the entire application"
  (:require [project-factual.views.items-list :as items-list]
            [project-factual.editor.editor :as markdown-editor]
            [project-factual.views.sidebar-groups :as sidebar-groups]
            [re-frame.core :as r]))

(defn main-page []
  (let [dimmed (r/subscribe [:screen-dim])]
    (fn []
      [:div {:class "main"}
       [sidebar-groups/sidebar-groups]
       [items-list/items-list]
       [markdown-editor/editor]
       [:div {:class (str "dim" (when-not @dimmed " hide"))
              :on-click #(r/dispatch [:set-sidebar-visibility false])}]])))